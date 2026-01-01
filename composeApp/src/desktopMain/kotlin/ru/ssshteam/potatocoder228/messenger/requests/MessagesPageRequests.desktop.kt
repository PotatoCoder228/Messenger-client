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
import kotlinx.io.RawSink
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.internal.File
import ru.ssshteam.potatocoder228.messenger.token

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
                    InputProvider { SystemFileSystem.source(Path(file.path)).buffered() },
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
    println(response.status)
}

actual suspend fun getMessageFileRequest(
    url: String,
    dest: RawSink?,
    onStart: () -> Unit,
    onEnd: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController, path: String
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
        while (!channel.exhausted()) {
            val chunk = channel.readRemaining()
            count += chunk.remaining
            chunk.transferTo(dest!!)
        }
        onEnd()
    }
}