package ru.ssshteam.potatocoder228.messenger

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import ru.ssshteam.potatocoder228.messenger.internal.File
import java.io.InputStream
import java.io.OutputStream


class AndroidFileChooser(activity: Activity) : FileChooser {

    private var context: Activity = activity
    private var mutex = Mutex(false)
    private var selectedFiles = mutableStateListOf<File>()
    private var selectedDownloadPath: String = ""
    private var inputStreams: MutableMap<String, InputStream> = mutableMapOf()
    private var outputStreams: MutableMap<String, OutputStream> = mutableMapOf()
    override suspend fun lockChooser() {
        mutex.lock()
    }

    override fun unlockChooser() {
        mutex.unlock()
    }

    override fun addFile(file: File) {
        selectedFiles.add(file)
    }

    override fun addFileOutputStream(path: String, stream: Any) {
        outputStreams[path] = stream as OutputStream
    }

    override fun removeFileOutputStream(path: String) {
        outputStreams.remove(path)
    }

    override fun removeFileInputStream(path: String) {
        inputStreams.remove(path)
    }

    override fun getFileOutputStream(path: String): Any {
        return outputStreams[path] as Any
    }

    override fun addFileInputStream(path: String, stream: Any) {
        inputStreams[path] = stream as InputStream
    }

    override fun getFileInputStream(path: String): Any {
        return inputStreams[path] as Any
    }

    override fun addPathForDownloading(path: String) {
        selectedDownloadPath = path
    }

    override suspend fun selectFile(): SnapshotStateList<File> {
        selectedFiles.clear()
        val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        val fileChooser = Intent.createChooser(intent, "Select a File to Upload")
        context.startActivityForResult(fileChooser, 2);
        lockChooser()
        while (mutex.isLocked) {
            delay(100)
        }
        return selectedFiles;
    }

    override suspend fun selectDownloadingFilepath(filename: String): String {
        val intent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, filename)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        val fileChooser = Intent.createChooser(intent, "Select a File to Download")
        context.startActivityForResult(fileChooser, 1);
        lockChooser()
        while (mutex.isLocked) {
            delay(100)
        }

        println("ABOBA unlocked")
        return selectedDownloadPath
    }
}