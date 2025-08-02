package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.runtime.snapshots.SnapshotStateList
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import java.io.File

actual suspend fun sendMessageFile(
    chatId: String,
    messageId: String,
    files: MutableList<ru.ssshteam.potatocoder228.messenger.internal.File>
) {
    val response: HttpResponse = httpClient.submitFormWithBinaryData(
        url = "$httpHost/chat/${chatId}/message/${messageId}/files",
        formData = formData {
            for (file in files) {
                append("files", File(file.path).readBytes(), Headers.build {
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