package ru.ssshteam.potatocoder228.messenger

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.MemoryCacheImageOutputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.TargetDataLine


class FastByteArrayOutputStream : ByteArrayOutputStream() {
    fun getBuf(): ByteArray {
        return this.buf
    }
}

fun bufferedImageToFrame(bufferedImage: BufferedImage?): Frame? {
    // Create an instance of the converter
    val converter = Java2DFrameConverter()

    // Convert the BufferedImage to a Frame
    val frame = converter.convert(bufferedImage)


    // Optional: The converter can be used multiple times, but if you are done,
    // you might release resources (though for Java2DFrameConverter it's less critical than OpenCVFrameConverter)
    converter.close()
    return frame
}

class WebViewModel : ViewModel() {
    var webcam: MutableState<Webcam>
    val stream = FastByteArrayOutputStream()
    val writer: ImageWriter
    val param: ImageWriteParam
    var recorder: FFmpegFrameRecorder? = FFmpegFrameRecorder(
        "./tmp.mp4",
        1280,
        720,
        2
    )

    // Create an instance of the converter
    var converter: Java2DFrameConverter? = null

    var isRecording2: Boolean = false
    var byteArrayOutputStream = ByteArrayOutputStream()
    var audioFormat2: AudioFormat? = null
    var targetDataLine: TargetDataLine? = null
    var audioInputStream: AudioInputStream? = null
    var sourceDataLine: SourceDataLine? = null

    val mutex: Mutex = Mutex()

    init {

        val nonStandardResolutions = arrayOf<Dimension?>(
            WebcamResolution.HD.size,
        )
        webcam = mutableStateOf(Webcam.getDefault())
        webcam.value.setCustomViewSizes(*nonStandardResolutions)
        webcam.value.setViewSize(WebcamResolution.HD.size)
        val writers = ImageIO.getImageWritersByFormatName("jpeg")
        if (!writers.hasNext()) {
            println("No JPEG writers found.")
        }
        writer = writers.next()
        param = writer.defaultWriteParam
        //param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
        // param.setCompressionQuality(1.0f) // Quality: 0.0f (low) to 1.0f (high)

        recorder!!.setFormat("mp4")
        recorder!!.setVideoCodec(avcodec.AV_CODEC_ID_H264) // H.264 is standard for MP4
        recorder!!.setFrameRate(30.0)
        recorder!!.videoBitrate = 6000000
        recorder!!.setPixelFormat(org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P) // Recommended pixel format
        recorder!!.setAudioCodec(avcodec.AV_CODEC_ID_AAC) // AAC is standard for MP4
        // We don't want variable bitrate audio
        recorder!!.setAudioOption("crf", "0")
        // Highest quality
        recorder!!.setAudioQuality(0.0)
        recorder!!.setSampleRate(44100)
        recorder!!.setAudioBitrate(128000) // Default bitrate if none found
        audioFormat2 = getAudioFormat()
        val dataLineInfo =
            DataLine.Info(
                TargetDataLine::class.java,
                audioFormat2
            )
        targetDataLine = AudioSystem.getLine(
            dataLineInfo
        ) as TargetDataLine?
    }

    private fun getAudioFormat(): AudioFormat {
        val sampleRate = 44100.0f
        //8000,11025,16000,22050,44100
        val sampleSizeInBits = 16
        //8,16
        val channels = 2
        //1,2
        val signed = true
        //true,false
        val bigEndian = false
        //true,false
        return AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian
        )
    }

    fun open() {
        webcam.value.open()
        converter = Java2DFrameConverter()
        targetDataLine!!.open(audioFormat2)
        targetDataLine!!.start()
        recorder!!.start()
    }

    fun close() {
        webcam.value.close()
        val rec = recorder
        recorder = null
        rec?.stop()
        rec?.release()
        converter?.close()
        targetDataLine!!.stop()
        byteArrayOutputStream.close()
    }

    fun recordSample() {
        if (isRecording2 == true) return
        isRecording2 = true
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                while (recorder != null) {
                    var tempBuffer: ByteArray? = ByteArray(2940)
                    val cnt = targetDataLine!!.read(
                        tempBuffer,
                        0,
                        tempBuffer!!.size
                    )
                    val nSamplesRead: Int = cnt / 2
                    val samples = ShortArray(nSamplesRead)
                    ByteBuffer.wrap(tempBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                        .get(samples)
                    val sBuff: ShortBuffer? = ShortBuffer.wrap(samples, 0, nSamplesRead)
                    if (cnt > 0) {
                        recorder?.recordSamples(44100, 2, sBuff)
                    }
                }
                isRecording2 = false
            }
        }
    }

    fun getImage(): ImageBitmap {
        stream.reset()
        writer.setOutput(MemoryCacheImageOutputStream(stream))
        val buf = webcam.value.getImage()
        if (buf != null) {
            writer.write(null, IIOImage(buf, null, null), param)
            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    recordSample()
                    val frame = converter?.convert(buf)
                    if (frame != null) {
                        recorder?.record(frame)
                    }
                }
            }
            return stream.getBuf().decodeToImageBitmap()
        }
        return ImageBitmap(1, 1)
    }
}


@Composable
actual fun WebCameraView(
    modifier: Modifier
) {
    val viewModel = remember { WebViewModel() }
    LaunchedEffect(viewModel) {
        viewModel.open()
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.close()
        }
    }

    var image by remember { mutableStateOf(viewModel.getImage()) }
    Canvas(modifier, onDraw = {
        image = viewModel.getImage()
        image.prepareToDraw()
        var scale = 1.0f
        if (image.height < size.height) {
            scale = size.height / image.height
        }
        scale(scale) {
            drawImage(
                image, dstOffset = _root_ide_package_.androidx.compose.ui.unit.IntOffset(
                    ((size.width - image.width) / 2).toInt(),
                    ((size.height - image.height) / 2).toInt()
                )
            )
        }
    })
}


class DesktopAudioRecorder() : AudioRecorder {

    var isRecording2: Boolean = false
    var tempBuffer: ByteArray? = ByteArray(10000)
    var byteArrayOutputStream: ByteArrayOutputStream? = null
    var audioFormat2: AudioFormat? = null
    var targetDataLine: TargetDataLine? = null
    var audioInputStream: AudioInputStream? = null
    var sourceDataLine: SourceDataLine? = null
    var recorder: FFmpegFrameRecorder? = FFmpegFrameRecorder(
        "./tmp.mp3",
        2
    )

    init {
        recorder!!.setFormat("mp3")
        recorder!!.setAudioCodec(avcodec.AV_CODEC_ID_MP3) // AAC is standard for MP4
        // We don't want variable bitrate audio
        recorder!!.setAudioOption("crf", "0")
        // Highest quality
        recorder!!.setAudioQuality(0.0)
        recorder!!.setSampleRate(44100)
        recorder!!.setAudioBitrate(192000) // Default bitrate if none found
    }

    private fun getAudioFormat(): AudioFormat {
        val sampleRate = 44100.0f
        //8000,11025,16000,22050,44100
        val sampleSizeInBits = 16
        //8,16
        val channels = 2
        //1,2
        val signed = true
        //true,false
        val bigEndian = false
        //true,false
        return AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian
        )
    }

    fun record() {
        byteArrayOutputStream =
            ByteArrayOutputStream()

        try {
            //Loop until stopCapture is set
            // другим потоком, который
            // обслуживает кнопку Stop.
            while (isRecording2) {
                // Считать данные из внутреннего
                // буфера строки данных.
                val cnt = targetDataLine!!.read(
                    tempBuffer,
                    0,
                    tempBuffer!!.size
                )

                val nSamplesRead: Int = cnt / 2
                val samples = ShortArray(nSamplesRead)
                ByteBuffer.wrap(tempBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                    .get(samples)
                val sBuff: ShortBuffer? = ShortBuffer.wrap(samples, 0, nSamplesRead)
                if (cnt > 0) {
                    recorder?.recordSamples(44100, 2, sBuff)
                }
            }

            println("Save audio $isRecording2")

        } catch (e: java.lang.Exception) {
            println(e)
        }
    }

    private fun captureAudio() {
        try {
            isRecording2 = true
            audioFormat2 = getAudioFormat()
            val dataLineInfo =
                DataLine.Info(
                    TargetDataLine::class.java,
                    audioFormat2
                )
            targetDataLine = AudioSystem.getLine(
                dataLineInfo
            ) as TargetDataLine?
            targetDataLine!!.open(audioFormat2)
            targetDataLine!!.start()
            recorder!!.start()
            record()
            recorder!!.stop()
        } catch (e: java.lang.Exception) {
            println(e)
        }
    }

    private fun playAudio() {
        try {
            // Подготовить все для
            // воспроизведения.
            // Получить ранее сохраненные данные
            // в объект массива байтов.
            val audioData =
                byteArrayOutputStream?.toByteArray()
            // Получить входной поток в
            // массиве байтов, содержащем данные
            val byteArrayInputStream
                    : InputStream = ByteArrayInputStream(
                audioData
            )
            val audioFormat =
                getAudioFormat()
            audioInputStream =
                AudioInputStream(
                    byteArrayInputStream,
                    audioFormat,
                    (audioData!!.size / audioFormat.getFrameSize
                        ()).toLong()
                )
            val dataLineInfo =
                DataLine.Info(
                    SourceDataLine::class.java,
                    audioFormat
                )
            sourceDataLine = AudioSystem.getLine(
                dataLineInfo
            ) as SourceDataLine?
            sourceDataLine!!.open(audioFormat)
            sourceDataLine!!.start()
            playRecord()
        } catch (e: Exception) {
            println(e)
        }
    }

    fun playRecord() {
        try {
            var cnt: Int
            // Продолжать цикл до тех пор,
            // пока метод read не вернет -1
            // для пустого потока.
            while ((audioInputStream!!.read
                    (
                    tempBuffer, 0,
                    tempBuffer!!.size
                ).also { cnt = it }) != -1
            ) {
                println("Play audio $cnt")
                if (cnt > 0) {
                    // Записать данные во внутренний
                    // буфер линии данных,
                    // откуда они будут доставлены
                    // на устройство воспроизведения.
                    sourceDataLine!!.write(
                        tempBuffer, 0, cnt
                    )
                } //конец if
            } //конец while
            println("Play audio $cnt")
            // Заблокировать и дождаться,
            // пока внутренний буфер линии
            // данных опустеет.
            sourceDataLine!!.drain()
            sourceDataLine!!.close()
        } catch (e: java.lang.Exception) {
            println(e)
        }
    }

    override fun record(path: String, stream: Any): String {
        captureAudio()
        return ""
    }

    override fun isRecording(): Boolean {
        return isRecording2
    }

    override fun stopRecord() {
        isRecording2 = false
        //playAudio()
    }

    override fun play(path: String) {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun stopPlaying() {
        TODO("Not yet implemented")
    }

}