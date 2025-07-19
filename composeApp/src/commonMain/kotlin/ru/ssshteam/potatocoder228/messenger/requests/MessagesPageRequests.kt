package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import kotlin.uuid.ExperimentalUuidApi

class MessagesPageRequests {
    companion object {

        private suspend fun errorAction(
            httpResponse: HttpResponse, snackbarHostState: SnackbarHostState
        ) {
            snackbarHostState.showSnackbar(
                message = "Error! Reason: ${httpResponse}, ${httpResponse.status.description}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        private suspend fun exceptionAction(e: Throwable, snackbarHostState: SnackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Exception! Reason: ${e.message}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        suspend fun getChatsRequest(
            snackbarHostState: SnackbarHostState,
            onChatsChange: (ChatDTO) -> Unit,
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.get("$httpHost/chats") {
                    headers {
                        header("Authorization", "Bearer ${token?.value?.token}")
                    }
                    contentType(ContentType.Application.Json)
                }
                if (httpResponse.status.value in 200..299) {
                    httpResponse.body<List<ChatDTO>>().forEach(onChatsChange)
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun addChatRequest(
            chatCreateDTO: ChatCreateDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.post("$httpHost/chat") {
                    headers {
                        header("Authorization", "Bearer ${token?.value?.token}")
                    }
                    contentType(ContentType.Application.Json)
                    setBody(chatCreateDTO)
                }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    println(httpResponse.status.value)
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {

                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun sendMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.post("$httpHost/chat/${chatDTO?.id}/send") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                        setBody(messageDTO)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun deleteMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.delete("$httpHost/chat/${chatDTO?.id}/message/${messageDTO?.id}") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                        setBody(messageDTO)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun updateMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.post("$httpHost/chat/${chatDTO?.id}/update") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                        setBody(messageDTO)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun updateLastEnterRequest(
            chatDTO: ChatDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.put("$httpHost/chat/${chatDTO?.id}/enter") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun updateLastThreadEnterRequest(
            chatDTO: ChatDTO?,
            msgDTO: MessageDTO,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.put("$httpHost/chat/${chatDTO?.id}/message/${msgDTO.id}/enter") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun deleteChatRequest(
            chatDTO: ChatDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.delete("$httpHost/chat/${chatDTO?.id}") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun getChatMessagesRequest(
            chatDTO: ChatDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO?.id ?: 0}/messages") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
                    httpResponse.body<List<MessageDTO>>().forEach(onMessagesChange)
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun sendThreadMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.post("$httpHost/chat/${chatDTO?.id}/thread/${messageDTO?.threadParentMsgId.toString()}/send") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                        setBody(messageDTO)
                    }
                if (httpResponse.status.value in 200..299) {
//                    onChatsChange(httpResponse.body())
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }

        suspend fun getThreadMessagesRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO?.id ?: 0}/thread/${messageDTO?.id ?: 0}/messages") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
                    httpResponse.body<List<MessageDTO>>().forEach(onMessagesChange)
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }
    }
}