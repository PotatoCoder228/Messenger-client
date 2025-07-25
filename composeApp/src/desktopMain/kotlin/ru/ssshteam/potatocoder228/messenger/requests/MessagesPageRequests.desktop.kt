package ru.ssshteam.potatocoder228.messenger.requests

import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.InternalAPI
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import java.io.File

@OptIn(InternalAPI::class)
actual suspend fun sendMessageFile(
    chatId: String,
    messageId: String,
    fullPath: String,
    filename: String
) {
    println("$chatId $fullPath")
    val response: HttpResponse = httpClient.submitFormWithBinaryData(
        url = "$httpHost/chat/${chatId}/file",
        formData = formData {
            append("files", File(fullPath).readBytes(), Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=\"ktor_logo.png\"")
            })
//            append("files", File(fullPath).readBytes(), Headers.build {
//                append(HttpHeaders.ContentType, "image/png")
//                append(HttpHeaders.ContentDisposition, "filename=\"ktor_logo.png\"")
//            })
        },
        block = {
            headers {
                header("Authorization", "Bearer ${token?.value?.token}")
            }
        }
    )
    println(response.status)
}