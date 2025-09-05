package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import ru.ssshteam.potatocoder228.messenger.PageRoutes
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.dto.UserInChatDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import kotlin.uuid.ExperimentalUuidApi

suspend fun sendMessageFile(
    chatId: String,
    messageId: String = "",
    files: MutableList<ru.ssshteam.potatocoder228.messenger.internal.File>,
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


class MessagesPageRequests {
    companion object {

        private suspend fun errorAction(
            httpResponse: HttpResponse,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
        ) {
            snackbarHostState.showSnackbar(
                message = "Error! Reason: ${httpResponse}, ${httpResponse.status.description}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        private suspend fun exceptionAction(
            e: Throwable,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
        ) {
            snackbarHostState.showSnackbar(
                message = "Exception! Reason: ${e.message}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        suspend fun getChatsRequest(
            snackbarHostState: SnackbarHostState,
            onChatsChange: (ChatDTO) -> Unit,
            navController: NavHostController
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
                } else if (httpResponse.status.value in 400..499) {
                    navController.navigate(PageRoutes.SignInPage.route)
                } else {
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        suspend fun addChatRequest(
            chatCreateDTO: ChatCreateDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {

                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun sendMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
        ): MessageDTO {
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
                    return httpResponse.body()
                } else {
                    errorAction(httpResponse, snackbarHostState, navController)
                    return MessageDTO()
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
                return MessageDTO()
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun deleteMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun updateMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun updateLastEnterRequest(
            chatDTO: ChatDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun updateLastThreadEnterRequest(
            chatDTO: ChatDTO?,
            msgDTO: MessageDTO,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun deleteChatRequest(
            chatDTO: ChatDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun getChatMessagesRequest(
            chatDTO: ChatDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun getChatProfilesRequest(
            chatDTO: ChatDTO?,
            snackbarHostState: SnackbarHostState,
            onProfilesChange: (UserInChatDTO) -> Unit,
            navController: NavHostController
        ) {
            try {
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO?.id ?: 0}/users") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
                    httpResponse.body<List<UserInChatDTO>>().forEach(onProfilesChange)
                } else {
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun sendThreadMessageRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            onChatsChange: (ChatDTO) -> Unit,
            snackbarHostState: SnackbarHostState,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun getThreadMessagesRequest(
            chatDTO: ChatDTO?,
            messageDTO: MessageDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
            navController: NavHostController
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
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }
    }
}