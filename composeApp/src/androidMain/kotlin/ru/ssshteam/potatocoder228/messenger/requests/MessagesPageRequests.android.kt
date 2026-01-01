package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import io.ktor.client.call.body
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.RawSink
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import ru.ssshteam.potatocoder228.messenger.fileChooser
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.internal.File
import ru.ssshteam.potatocoder228.messenger.token
import java.io.InputStream
import java.io.OutputStream

actual suspend fun sendMessageFile(
    chatId: String,
    messageId: String,
    files: MutableList<File>,
    navController: NavHostController
) {
    val response: HttpResponse = httpClient.submitFormWithBinaryData(
        url = "$httpHost/chat/${chatId}/message/${messageId}/files",
        formData = formData {
            for (file in files) {
                append(
                    "files",
                    InputProvider { (fileChooser?.getFileInputStream(file.path) as InputStream).asInput().buffered() },
                    Headers.build {
                        append(HttpHeaders.ContentType, "multipart/form-data")
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.filename}\"")
                    })
            }
        },
        block = {
            headers {
                header("Authorization", "Bearer ${token?.value?.token}")
            }
        }
    )
    for (file in files) {
        withContext(Dispatchers.IO) {
            (fileChooser?.getFileInputStream(file.path) as InputStream).close()
            fileChooser?.removeFileInputStream(file.path)
        }
    }
    println(response.status)
}

actual suspend fun getMessageFileRequest(
    url: String,
    dest: RawSink?,
    onStart: () -> Unit,
    onEnd: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    path: String
) {
    httpClient.prepareGet("$httpHost${url}") {
        headers {
            header("Authorization", "Bearer ${token?.value?.token}")
        }
        contentType(ContentType.Application.Json)
    }.execute { httpResponse ->
        val channel: ByteReadChannel = httpResponse.body()
        var count = 0L
        onStart()
        val sink = (fileChooser?.getFileOutputStream(path) as OutputStream).asSink()
        while (!channel.exhausted()) {
            val chunk = channel.readRemaining()
            count += chunk.remaining
            chunk.transferTo(sink)
        }
        (fileChooser?.getFileOutputStream(path) as OutputStream).close()
        fileChooser?.removeFileOutputStream(path)
        onEnd()
    }
}