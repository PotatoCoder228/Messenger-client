package ru.ssshteam.potatocoder228.messenger.viewmodels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.use
import ru.ssshteam.potatocoder228.messenger.UiState
import ru.ssshteam.potatocoder228.messenger.client
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.dto.NotificationDTO
import ru.ssshteam.potatocoder228.messenger.dto.OperationDTO
import ru.ssshteam.potatocoder228.messenger.dto.UserInChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.internal.FileView
import ru.ssshteam.potatocoder228.messenger.internal.File
import ru.ssshteam.potatocoder228.messenger.json
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.deleteChatRequest
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.getChatsRequest
import ru.ssshteam.potatocoder228.messenger.requests.sendMessageFile
import ru.ssshteam.potatocoder228.messenger.token
import ru.ssshteam.potatocoder228.messenger.wsHost
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MessagesViewModel : ViewModel() {
    @OptIn(ExperimentalAtomicApi::class)
    var chatsSessionIdentificator: AtomicInt = AtomicInt(0)
    private val chatsSessionMutex = Mutex()
    private var chatsCurrentSession: StompSession? = null
    var expanded = mutableStateOf(false)
    var editingMode = mutableStateOf(false)
    var threadEditingMode = mutableStateOf(false)
    val snackbarChatsHostState = SnackbarHostState()
    var addChatDialogExpanded = mutableStateOf(false)
    val chatNameInput = mutableStateOf("")
    var key = mutableStateOf(0)

    var usersMap = mutableStateListOf(mutableStateOf(Pair(key.value, "")))

    @OptIn(ExperimentalAtomicApi::class)
    var messagesSessionIdentificator: AtomicInt = AtomicInt(0)
    private val messagesSessionMutex = Mutex()
    private var messagesCurrentSession: StompSession? = null

    @OptIn(ExperimentalAtomicApi::class)
    var notificationsSessionIdentificator: AtomicInt = AtomicInt(0)
    private val notificationsSessionMutex = Mutex()
    private var notificationsCurrentSession: StompSession? = null
    val chats = mutableStateListOf<ChatDTO?>()

    val navRailVisible = mutableStateOf(false)

    val userProfileVisible = mutableStateOf(false)

    @OptIn(ExperimentalUuidApi::class)
    val profileId = mutableStateOf(Uuid.NIL)

    val mainSnackbarHostState = mutableStateOf(SnackbarHostState())
    val messages = mutableStateListOf<MessageDTO?>()
    val chatProfiles = mutableStateListOf<UserInChatDTO?>()
    val threadMessages = mutableStateListOf<MessageDTO?>()

    val selectedChatMembers = mutableIntStateOf(0)
    val selectedChatMembersOnline = mutableIntStateOf(0)


    val message = mutableStateOf("")
    val editingMsgOrigText = mutableStateOf("")
    val threadMessage = mutableStateOf("")
    val editingThreadMsgOrigText = mutableStateOf("")
    var selectedChat = mutableStateOf<ChatDTO?>(null)
    var selectedMsg = mutableStateOf<MessageDTO?>(null)
    var detailPaneState = mutableStateOf(UiState.Loading)

    val fromExtraToDetail = mutableStateOf(false)
    val fromDetailToList = mutableStateOf(false)

    val msgSettingsModifier: MutableState<Modifier?> = mutableStateOf(null)
    val msgSettingsDropdownMenuModifier: MutableState<Modifier?> = mutableStateOf(null)


    val msgBoxModifier: MutableState<Modifier?> = mutableStateOf(null)
    val msgTextModifier: MutableState<Modifier?> = mutableStateOf(null)
    val msgsColumnModifier: MutableState<Modifier?> = mutableStateOf(null)
    val msgSenderTextModifier: MutableState<Modifier?> = mutableStateOf(null)


    val chatSettingsDropdownMenuModifier: MutableState<Modifier?> = mutableStateOf(null)
    val chatBoxModifier: MutableState<Modifier?> = mutableStateOf(null)
    val chatNameTextModifier: MutableState<Modifier?> = mutableStateOf(null)
    val chatBadgeCounterModifier: MutableState<Modifier?> = mutableStateOf(null)
    val threadBadgeCounterModifier: MutableState<Modifier?> = mutableStateOf(null)

    @OptIn(ExperimentalUuidApi::class)
    val msgDTO: MutableState<MessageDTO> = mutableStateOf(MessageDTO())

    @OptIn(ExperimentalUuidApi::class)
    val threadMsgDTO: MutableState<MessageDTO> = mutableStateOf(MessageDTO())

    fun loadChats(navController: NavHostController) {
        viewModelScope.launch {
            getChatsRequest(
                snackbarHostState = mainSnackbarHostState.value, onChatsChange = { chat ->
                    chat.unreadedMessages = chat.newMessages!!
                    chats.add(chat)
                }, navController
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun showChatMessages(
        chatDTO: ChatDTO?,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            if (chatDTO != null) {
                val first = chats.withIndex().firstOrNull {
                    (chatDTO.id) == (it.value?.id)
                }
                val chatCopy = chatDTO.copy(unreadedMessages = 0)
                selectedChat.value = chatCopy
                if (first != null) {
                    chats[first.index] = chatCopy
                }

            }
            detailPaneState.value = UiState.Loading
            messages.clear()
            MessagesPageRequests.getChatMessagesRequest(
                chatDTO, mainSnackbarHostState.value,
                { message ->
                    for (file in message.filesUrls) {
                        println(file.name)
                    }
                    message.unreadMessages = message.newThreadMessages
                    messages.add(
                        message
                    )
                }, navController
            )
            detailPaneState.value = UiState.Loaded
        }
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    fun navigateToChats(
        scope: CoroutineScope, scaffoldNavigator: ThreePaneScaffoldNavigator<String>
    ) {
        viewModelScope.launch {
            withContext(scope.coroutineContext) {
                selectedChat.value = null
                detailPaneState.value = UiState.Loading
                scaffoldNavigator.navigateTo(
                    ListDetailPaneScaffoldRole.Detail
                )
            }
        }
    }

    fun addChat(navController: NavHostController) {
        viewModelScope.launch {
            val usersList: MutableList<String> = mutableListOf()
            usersMap.forEach { userPair ->
                usersList.add(userPair.value.second)
            }
            MessagesPageRequests.addChatRequest(
                ChatCreateDTO(
                    chatNameInput.value, usersList
                ), { chat -> }, snackbarChatsHostState, navController
            )

            addChatDialogExpanded.value = false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendMessage(
        lazyColumnListState: LazyListState,
        selectedFiles: MutableList<File>,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            msgDTO.value = MessageDTO()
            msgDTO.value.message = message.value
            msgDTO.value.messageType = "REGULAR"
            val addedMessageDTO = MessagesPageRequests.sendMessageRequest(
                selectedChat.value,
                msgDTO.value,
                { item -> },
                mainSnackbarHostState.value, navController
            )
            if (addedMessageDTO.id != Uuid.NIL && selectedFiles.size != 0) {
                sendMessageFile(
                    chatId = selectedChat.value?.id!!.toString(),
                    messageId = addedMessageDTO.id.toString(),
                    files = selectedFiles,
                    navController
                )
            } else {
                println("uuid is null! ${addedMessageDTO.id}")
            }
            selectedFiles.clear()
            lazyColumnListState.scrollToItem(0)
            message.value = ""
        }
    }

    fun showChatProfiles(
        chatDTO: ChatDTO?,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            if (chatDTO != null) {
                chatProfiles.clear()
                MessagesPageRequests.getChatProfilesRequest(
                    chatDTO, mainSnackbarHostState.value, { profile ->
                        println(profile.login)
                        chatProfiles.add(
                            profile
                        )
                    }, navController
                )
                selectedChatMembers.value = chatProfiles.size
                selectedChatMembersOnline.value =
                    chatProfiles.count { item -> item?.status == "ONLINE" }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendThreadMessage(
        lazyColumnListState: LazyListState,
        selectedFiles: MutableList<File>,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            threadMsgDTO.value = MessageDTO()
            threadMsgDTO.value.message = threadMessage.value
            threadMsgDTO.value.messageType = "THREAD"
            threadMsgDTO.value.threadParentMsgId = selectedMsg.value!!.id
            val addedMessageDTO = MessagesPageRequests.sendThreadMessageRequest(
                selectedChat.value,
                threadMsgDTO.value,
                { item -> },
                mainSnackbarHostState.value,
                navController
            )

            if (addedMessageDTO.id != Uuid.NIL && selectedFiles.size != 0) {
                sendMessageFile(
                    chatId = selectedChat.value?.id!!.toString(),
                    messageId = addedMessageDTO.id.toString(),
                    files = selectedFiles,
                    navController
                )
            } else {
                println("uuid is null! ${addedMessageDTO.id}")
            }
            selectedFiles.clear()
            lazyColumnListState.scrollToItem(threadMessages.size - 1)
            threadMessage.value = ""
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun showChatThreadMessages(
        chatDTO: ChatDTO?,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            threadMessages.clear()
            val msgCopy = selectedMsg.value?.copy(unreadMessages = 0)
            val first = messages.withIndex().firstOrNull {
                (selectedMsg.value?.id) == (it.value?.id)
            }

            selectedMsg.value = msgCopy
            if (first != null) {
                messages[first.index] = msgCopy
            }
            MessagesPageRequests.getThreadMessagesRequest(
                chatDTO, selectedMsg.value, mainSnackbarHostState.value, { message ->
                    for (file in message.filesUrls) {
                        println(file.name)
                    }
                    threadMessages.add(
                        message
                    )
                }, navController
            )
        }
    }

    fun deleteMessage(navController: NavHostController) {
        viewModelScope.launch {
            MessagesPageRequests.deleteMessageRequest(
                selectedChat.value,
                msgDTO.value,
                { item -> },
                mainSnackbarHostState.value,
                navController
            )
        }
    }

    fun updateMessage(navController: NavHostController) {
        viewModelScope.launch {
            msgDTO.value.message = message.value
            MessagesPageRequests.updateMessageRequest(
                selectedChat.value,
                msgDTO.value,
                { item -> },
                mainSnackbarHostState.value,
                navController
            )
            message.value = ""
            editingMode.value = false
        }
    }

    fun updateThreadMessage(navController: NavHostController) {
        viewModelScope.launch {
            threadMsgDTO.value.message = threadMessage.value
            MessagesPageRequests.updateMessageRequest(
                selectedChat.value,
                threadMsgDTO.value,
                { item -> },
                mainSnackbarHostState.value,
                navController
            )
            threadMessage.value = ""
            threadEditingMode.value = false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun deleteChat(chatDTO: ChatDTO?, navController: NavHostController) {
        viewModelScope.launch {
            if (selectedChat.value?.id == chatDTO?.id) {
                selectedChat.value = null
            }
            deleteChatRequest(
                chatDTO, { chat ->
                }, mainSnackbarHostState.value,
                navController
            )
        }
    }

    fun expandAddChatDialog() {
        addChatDialogExpanded.value = true
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalUuidApi::class)
    fun subscribeToUpdates() {
        viewModelScope.launch {
            val identificator = chatsSessionIdentificator.addAndFetch(1)
            val session: StompSession = client.connect(
                wsHost,
                customStompConnectHeaders = mapOf("Authorization" to "Bearer ${token?.value?.token}")
            )
            chatsSessionMutex.withLock {
                if (chatsCurrentSession != null) {
                    chatsCurrentSession?.disconnect()
                }
                chatsCurrentSession = session
            }
            session.use {
                val jsonStompSession = session.withJsonConversions(json)
                jsonStompSession.use { s ->
                    val subscription: Flow<OperationDTO<ChatDTO>> = s.subscribe(
                        StompSubscribeHeaders("/topic/user/${token?.value?.userId}/chats"),
                        OperationDTO.serializer(ChatDTO.serializer())
                    )
                    while (identificator == chatsSessionIdentificator.load()) {
                        subscription.collect { stompMessage ->
                            val first = chats.withIndex().firstOrNull {
                                (stompMessage.data?.id) == (it.value?.id)
                            }
                            if (identificator != chatsSessionIdentificator.load()) {
                                return@collect
                            }
                            when (stompMessage.operation) {
                                "ADD" -> {
                                    chats.add(
                                        0, stompMessage.data
                                    )
                                }

                                "DELETE" -> {
                                    if (first != null) {
                                        chats.removeAt(first.index)
                                    }
                                }

                                "UPDATE" -> {
                                    if (first != null) {
                                        chats[first.index] = stompMessage.data
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalUuidApi::class)
    fun subscribeToMessagesUpdates(
        lazyColumnListState: LazyListState,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val sessionId = messagesSessionIdentificator.addAndFetch(1)
            val session: StompSession = client.connect(
                wsHost,
                customStompConnectHeaders = mapOf("Authorization" to "Bearer ${token?.value?.token}")
            )
            messagesSessionMutex.withLock {
                if (messagesCurrentSession != null) {
                    messagesCurrentSession?.disconnect()
                }
                messagesCurrentSession = session
            }
            session.use {
                val jsonStompSession = session.withJsonConversions(json)
                jsonStompSession.use { s ->
                    val subscription: Flow<OperationDTO<MessageDTO>> = s.subscribe(
                        StompSubscribeHeaders("/topic/chat/${selectedChat.value?.id}/messages"),
                        OperationDTO.serializer(
                            MessageDTO.serializer()
                        )
                    )
                    while (sessionId == messagesSessionIdentificator.load()) {
                        subscription.collect { stompMessage ->
                            if (sessionId != messagesSessionIdentificator.load()) {
                                return@collect
                            }
                            when (stompMessage.operation) {
                                "ADD" -> {
                                    if (stompMessage.data?.messageType == "REGULAR") {
                                        val index = lazyColumnListState.firstVisibleItemIndex
                                        messages.add(
                                            0, stompMessage.data
                                        )
                                        if (index == 0) {
                                            lazyColumnListState.scrollToItem(
                                                0
                                            )
                                        }
                                    } else if (stompMessage.data?.threadParentMsgId == selectedMsg.value?.id && stompMessage.data?.messageType == "THREAD") {
                                        threadMessages.add(
                                            stompMessage.data
                                        )
                                    }
                                }

                                "DELETE" -> {
                                    if (stompMessage.data?.messageType == "REGULAR") {
                                        val first = messages.withIndex().firstOrNull {
                                            (stompMessage.data?.id ?: 0) == (it.value?.id ?: 0)
                                        }
                                        if (first != null) {
                                            messages.removeAt(
                                                first.index
                                            )
                                        }
                                    } else if (stompMessage.data?.messageType == "THREAD") {
                                        val first = threadMessages.withIndex().firstOrNull {
                                            (stompMessage.data?.id ?: 0) == (it.value?.id ?: 0)
                                        }
                                        if (first != null) {
                                            threadMessages.removeAt(
                                                first.index
                                            )
                                        }
                                    }
                                }

                                "UPDATE" -> {
                                    if (stompMessage.data?.messageType == "REGULAR") {
                                        val first = messages.withIndex().firstOrNull {
                                            (stompMessage.data?.id ?: 0) == (it.value?.id ?: 0)
                                        }
                                        if (first != null) {
                                            messages[first.index] = stompMessage.data
                                        }
                                    } else if (stompMessage.data?.messageType == "THREAD") {
                                        val first = threadMessages.withIndex().firstOrNull {
                                            (stompMessage.data?.id ?: 0) == (it.value?.id ?: 0)
                                        }
                                        println("${first?.index}")
                                        if (first != null) {
                                            threadMessages[first.index] = stompMessage.data
                                        }
                                    }
                                }
                            }
                            MessagesPageRequests.updateLastEnterRequest(
                                selectedChat.value,
                                { item -> },
                                mainSnackbarHostState.value,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalUuidApi::class)
    fun subscribeToNotifications(navController: NavHostController) {
        viewModelScope.launch {
            val identificator = notificationsSessionIdentificator.addAndFetch(1)
            val session: StompSession = client.connect(
                wsHost,
                customStompConnectHeaders = mapOf("Authorization" to "Bearer ${token?.value?.token}")
            )
            notificationsSessionMutex.withLock {
                if (notificationsCurrentSession != null) {
                    notificationsCurrentSession?.disconnect()
                }
                notificationsCurrentSession = session
            }
            session.use {
                val jsonStompSession = session.withJsonConversions(json)
                jsonStompSession.use { s ->
                    val subscription: Flow<NotificationDTO> = s.subscribe(
                        StompSubscribeHeaders("/topic/user/${token?.value?.userId}/notifications"),
                        NotificationDTO.serializer()
                    )
                    while (identificator == notificationsSessionIdentificator.load()) {
                        subscription.collect { stompMessage ->
                            if (identificator != notificationsSessionIdentificator.load()) {
                                return@collect
                            }
                            when (stompMessage.type) {
                                "NEW_MESSAGE" -> {
                                    println("NEW_MESSAGE")
                                    try {
                                        val first = chats.withIndex().firstOrNull {
                                            (stompMessage.chatId) == (it.value?.id)
                                        }
                                        println("NEW_MESSAGE2")
                                        if (first != null && selectedChat.value?.id != first.value?.id) {
                                            println("NEW_MESSAGE3")
                                            chats[first.index] =
                                                chats[first.index]?.copy(unreadedMessages = first.value?.unreadedMessages!! + 1)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                "NEW_THREAD_MESSAGE" -> {
                                    println("NEW_THREAD_MESSAGE")
                                    try {
                                        val jsonElement = Json.parseToJsonElement(stompMessage.data)
                                        val first = messages.withIndex().firstOrNull {
                                            val id = jsonElement.jsonObject["threadId"].toString()
                                            (id.subSequence(
                                                1,
                                                id.length - 1
                                            )) == (it.value?.id.toString())
                                        }
                                        if (first != null && selectedMsg.value?.id != first.value?.id) {
                                            messages[first.index] =
                                                messages[first.index]?.copy(unreadMessages = first.value?.unreadMessages!! + 1)
                                        } else if (first != null && selectedMsg.value != null && selectedMsg.value?.id == first.value?.id) {
                                            MessagesPageRequests.updateLastThreadEnterRequest(
                                                selectedChat.value,
                                                selectedMsg.value!!,
                                                { item -> },
                                                mainSnackbarHostState.value,
                                                navController
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                "NEW_CHAT_UPDATE_ON_SEND" -> {
                                    println("NEW_CHAT_UPDATE_ON_SEND")
                                    try {
                                        val jsonElement = Json.parseToJsonElement(stompMessage.data)
                                        val first = chats.withIndex().firstOrNull {
                                            val id = jsonElement.jsonObject["chatId"].toString()
                                            (id.subSequence(
                                                1,
                                                id.length - 1
                                            )) == (it.value?.id.toString())
                                        }
                                        if (first != null && selectedMsg.value?.id != first.value?.id) {
                                            val sendAt =
                                                jsonElement.jsonObject["lastMsgSendAt"].toString()
                                            val msgOwner =
                                                jsonElement.jsonObject["lastMsgOwner"].toString()
                                            val msgData =
                                                jsonElement.jsonObject["lastMsgData"].toString()
                                            chats[first.index] =
                                                chats[first.index]?.copy(
                                                    lastMsgOwner = msgOwner.subSequence(
                                                        1,
                                                        msgOwner.length - 1
                                                    ).toString(),
                                                    lastMsgData = msgData.subSequence(
                                                        1,
                                                        msgData.length - 1
                                                    ).toString(),
                                                    lastMsgSendAt = LocalDateTime.parse(
                                                        sendAt.subSequence(
                                                            1,
                                                            sendAt.length - 1
                                                        )
                                                    ),
                                                )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                "NEW_CHAT_UPDATE_ON_DELETE" -> {
                                    println("NEW_CHAT_UPDATE_ON_DELETE")
                                    try {
                                        val jsonElement = Json.parseToJsonElement(stompMessage.data)
                                        val first = chats.withIndex().firstOrNull {
                                            val id = jsonElement.jsonObject["chatId"].toString()
                                            (id.subSequence(
                                                1,
                                                id.length - 1
                                            )) == (it.value?.id.toString())
                                        }
                                        if (first != null && selectedMsg.value?.id != first.value?.id) {
                                            val sendAt =
                                                jsonElement.jsonObject["lastMsgSendAt"].toString()
                                            val msgOwner =
                                                jsonElement.jsonObject["lastMsgOwner"].toString()
                                            val msgData =
                                                jsonElement.jsonObject["lastMsgData"].toString()
                                            chats[first.index] =
                                                chats[first.index]?.copy(
                                                    lastMsgOwner = msgOwner.subSequence(
                                                        1,
                                                        msgOwner.length - 1
                                                    ).toString(),
                                                    lastMsgData = msgData.subSequence(
                                                        1,
                                                        msgData.length - 1
                                                    ).toString(),
                                                    lastMsgSendAt = LocalDateTime.parse(
                                                        sendAt.subSequence(
                                                            1,
                                                            sendAt.length - 1
                                                        )
                                                    ),
                                                )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                "NEW_SEND_FILE" -> {
                                    println("NEW_SEND_FILE")
                                    try {
                                        val jsonElement = Json.parseToJsonElement(stompMessage.data)
                                        for (view in jsonElement.jsonArray) {
                                            val first = messages.withIndex().firstOrNull {
                                                val id = view.jsonObject["messageId"].toString()
                                                (id.subSequence(
                                                    1,
                                                    id.length - 1
                                                )) == (it.value?.id.toString())
                                            }
                                            if (first != null) {
                                                val id =
                                                    view.jsonObject["id"].toString()
                                                val messageId =
                                                    view.jsonObject["messageId"].toString()
                                                val name =
                                                    view.jsonObject["name"].toString()
                                                val contentType =
                                                    view.jsonObject["contentType"].toString()
                                                val size =
                                                    view.jsonObject["size"].toString()
                                                val url =
                                                    view.jsonObject["url"].toString()
                                                messages[first.index] =
                                                    messages[first.index]?.copy(
                                                        filesUrls = messages[first.index]!!.filesUrls.plusElement(
                                                            FileView(
                                                                Uuid.parse(
                                                                    id.subSequence(
                                                                        1,
                                                                        id.length - 1
                                                                    ).toString()
                                                                ),
                                                                Uuid.parse(
                                                                    messageId.subSequence(
                                                                        1,
                                                                        messageId.length - 1
                                                                    ).toString()
                                                                ),
                                                                name.subSequence(
                                                                    1,
                                                                    name.length - 1
                                                                ).toString(),
                                                                contentType.subSequence(
                                                                    1,
                                                                    contentType.length - 1
                                                                ).toString(),
                                                                size.subSequence(
                                                                    1,
                                                                    size.length - 1
                                                                ).toString().toLong(),
                                                                url.subSequence(
                                                                    1,
                                                                    url.length - 1
                                                                ).toString()
                                                            )
                                                        )
                                                    )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                "NEW_SEND_THREAD_FILE" -> {
                                    println("NEW_SEND_THREAD_FILE")
                                    try {
                                        val jsonElement = Json.parseToJsonElement(stompMessage.data)
                                        for (view in jsonElement.jsonArray) {
                                            val first = threadMessages.withIndex().firstOrNull {
                                                val id = view.jsonObject["messageId"].toString()
                                                (id.subSequence(
                                                    1,
                                                    id.length - 1
                                                )) == (it.value?.id.toString())
                                            }
                                            if (first != null) {
                                                val id =
                                                    view.jsonObject["id"].toString()
                                                val messageId =
                                                    view.jsonObject["messageId"].toString()
                                                val name =
                                                    view.jsonObject["name"].toString()
                                                val contentType =
                                                    view.jsonObject["contentType"].toString()
                                                val size =
                                                    view.jsonObject["size"].toString()
                                                val url =
                                                    view.jsonObject["url"].toString()
                                                if (threadMessages[first.index] != null) {
                                                    threadMessages[first.index] =
                                                        threadMessages[first.index]?.copy(
                                                            filesUrls = threadMessages[first.index]!!.filesUrls.plusElement(
                                                                FileView(
                                                                    Uuid.parse(
                                                                        id.subSequence(
                                                                            1,
                                                                            id.length - 1
                                                                        ).toString()
                                                                    ),
                                                                    Uuid.parse(
                                                                        messageId.subSequence(
                                                                            1,
                                                                            messageId.length - 1
                                                                        ).toString()
                                                                    ),
                                                                    name.subSequence(
                                                                        1,
                                                                        name.length - 1
                                                                    ).toString(),
                                                                    contentType.subSequence(
                                                                        1,
                                                                        contentType.length - 1
                                                                    ).toString(),
                                                                    size.subSequence(
                                                                        1,
                                                                        size.length - 1
                                                                    ).toString().toLong(),
                                                                    url.subSequence(
                                                                        1,
                                                                        url.length - 1
                                                                    ).toString()
                                                                )
                                                            )
                                                        )
                                                }
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}