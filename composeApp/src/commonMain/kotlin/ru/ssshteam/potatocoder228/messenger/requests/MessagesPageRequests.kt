package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
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
import kotlinx.datetime.LocalDateTime
import kotlinx.io.RawSink
import ru.ssshteam.potatocoder228.messenger.PageRoutes
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.dto.UserInChatDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect suspend fun sendMessageFile(
    chatId: String,
    messageId: String = "",
    files: MutableList<ru.ssshteam.potatocoder228.messenger.internal.File>,
    navController: NavHostController
)

expect suspend fun getMessageFileRequest(
    url: String,
    dest: RawSink?,
    onStart: () -> Unit,
    onEnd: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    path: String
)

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
            cleanChats: () -> Unit,
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
                    cleanChats()
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

        suspend fun getNextChatsRequest(
            snackbarHostState: SnackbarHostState,
            onChatsChange: (ChatDTO) -> Unit,
            navController: NavHostController,
            after: LocalDateTime?
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.get("$httpHost/chats/next") {
                    headers {
                        header("Authorization", "Bearer ${token?.value?.token}")
                    }
                    if (after != null) {
                        url {
                            parameters.append("after", after.toString())
                        }
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

        suspend fun getPreviousChatsRequest(
            snackbarHostState: SnackbarHostState,
            onChatsChange: (ChatDTO) -> Unit,
            navController: NavHostController,
            before: LocalDateTime?
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.get("$httpHost/chats/previous") {
                    headers {
                        header("Authorization", "Bearer ${token?.value?.token}")
                    }
                    if (before != null) {
                        url {
                            parameters.append("before", before.toString())
                        }
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
            cleanMessages: () -> Unit,
            onMessagesChange: (MessageDTO) -> Unit,
            navController: NavHostController,
            before: LocalDateTime? = null
        ) {
            try {
                val id = chatDTO?.id
                if (id == Uuid.NIL || id == null) {
                    return
                }
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO.id}/messages") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        if (before != null) {
                            url {
                                parameters.append("before", before.toString())
                            }
                        }
                        contentType(ContentType.Application.Json)
                    }
                if (httpResponse.status.value in 200..299) {
                    cleanMessages()
                    httpResponse.body<List<MessageDTO>>().forEach(onMessagesChange)
                } else {
                    errorAction(httpResponse, snackbarHostState, navController)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState, navController)
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        suspend fun getNextChatMessagesRequest(
            chatDTO: ChatDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
            navController: NavHostController,
            after: LocalDateTime? = null
        ) {
            try {
                val id = chatDTO?.id
                if (id == Uuid.NIL || id == null) {
                    return
                }
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO.id}/messages/next") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        if (after != null) {
                            url {
                                parameters.append("after", after.toString())
                            }
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
        suspend fun getPreviousChatMessagesRequest(
            chatDTO: ChatDTO?,
            snackbarHostState: SnackbarHostState,
            onMessagesChange: (MessageDTO) -> Unit,
            navController: NavHostController,
            before: LocalDateTime? = null
        ) {
            try {
                val id = chatDTO?.id
                if (id == Uuid.NIL || id == null) {
                    return
                }
                val httpResponse: HttpResponse =
                    httpClient.get("$httpHost/chat/${chatDTO.id}/messages/previous") {
                        headers {
                            header("Authorization", "Bearer ${token?.value?.token}")
                        }
                        if (before != null) {
                            url {
                                parameters.append("before", before.toString())
                            }
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
        ): MessageDTO {
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