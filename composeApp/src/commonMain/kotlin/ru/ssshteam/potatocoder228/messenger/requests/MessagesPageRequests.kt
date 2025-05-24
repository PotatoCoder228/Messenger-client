package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token

class MessagesPageRequests {
    companion object {

        private suspend fun successAction(
            snackbarHostState: SnackbarHostState
        ) {

        }

        private suspend fun errorAction(
            httpResponse: HttpResponse, snackbarHostState: SnackbarHostState
        ) {
            snackbarHostState.showSnackbar(
                message = "Getting chats error. Reason: ${httpResponse.status.description}, ${httpResponse.status.description}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        private suspend fun exceptionAction(e: Throwable, snackbarHostState: SnackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Getting chats error. Reason: ${e.message}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
            e.printStackTrace()
        }

        suspend fun getChatsRequest(
            snackbarHostState: SnackbarHostState
        ): MutableList<ChatDTO> {
            try {
                val httpResponse: HttpResponse = httpClient.get("$httpHost/chats") {
                    headers {
                        header("Authorization", "Bearer ${token.token}")
                    }
                    contentType(ContentType.Application.Json)
                }
                if (httpResponse.status.value in 200..299) {
                    successAction(snackbarHostState)
                    return httpResponse.body()
                } else {
                    errorAction(httpResponse, snackbarHostState)
                    return mutableListOf()
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
                return mutableListOf()
            }
        }
    }
}