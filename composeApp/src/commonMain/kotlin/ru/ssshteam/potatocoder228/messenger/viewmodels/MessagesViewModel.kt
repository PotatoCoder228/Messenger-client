package ru.ssshteam.potatocoder228.messenger.viewmodels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.use
import ru.ssshteam.potatocoder228.messenger.UiState
import ru.ssshteam.potatocoder228.messenger.client
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.dto.OperationDTO
import ru.ssshteam.potatocoder228.messenger.json
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.deleteChatRequest
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.getChatsRequest
import ru.ssshteam.potatocoder228.messenger.token
import ru.ssshteam.potatocoder228.messenger.wsHost
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class MessagesViewModel : ViewModel() {

    @OptIn(ExperimentalAtomicApi::class)
    var chatsSessionIdentificator: AtomicInt = AtomicInt(0)
    val chatsSessionMutex = Mutex()
    var chatsCurrentSession: StompSession? = null
    var expanded = mutableStateOf(false)
    val snackbarChatsHostState = SnackbarHostState()
    var addChatDialogExpanded = mutableStateOf(false)
    val chatNameInput = mutableStateOf("")
    var key = mutableStateOf(0u)

    val users = mutableStateListOf(key.value)
    val usersMap = mutableStateOf(mutableMapOf<UInt, String>())

    @OptIn(ExperimentalAtomicApi::class)
    var messagesSessionIdentificator: AtomicInt = AtomicInt(0)
    val messagesSessionMutex = Mutex()
    var messagesCurrentSession: StompSession? = null
    val chats = mutableStateListOf<ChatDTO?>()

    val mainSnackbarHostState = mutableStateOf(SnackbarHostState())
    val messages = mutableStateListOf<MessageDTO?>()
    val message = mutableStateOf("")
    var selectedChat = mutableStateOf<ChatDTO?>(null)
    var detailPaneState = mutableStateOf(UiState.Loading)


    fun reverseNavigationSheetState(sheetState: DrawerState) {
        viewModelScope.launch {
            if (sheetState.isClosed) {
                sheetState.open()
            } else {
                sheetState.close()
            }
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            getChatsRequest(
                snackbarHostState = mainSnackbarHostState.value, onChatsChange = { chat ->
                    chats.add(chat)
                    println("Chat added ${chat.id}")
                })
        }
    }

    fun showChatMessages(
        chatDTO: ChatDTO?
    ) {
        viewModelScope.launch {
            if (chatDTO != null) {
                selectedChat.value = chatDTO
            }
            detailPaneState.value = UiState.Loading
            messages.clear()
            MessagesPageRequests.getChatMessagesRequest(
                chatDTO, mainSnackbarHostState.value
            ) { message ->
                messages.add(
                    message
                )
            }
            detailPaneState.value = UiState.Loaded
        }
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    fun navigateToChats(
        scope: CoroutineScope,
        scaffoldNavigator: ThreePaneScaffoldNavigator<String>
    ) {
        viewModelScope.launch {
            withContext(scope.coroutineContext) {
                selectedChat.value = null
                detailPaneState.value = UiState.Loading
                scaffoldNavigator.navigateTo(
                    ListDetailPaneScaffoldRole.List
                )
            }
        }
    }

    fun addChat() {
        viewModelScope.launch {
            val usersList: MutableList<String> = usersMap.value.values.toMutableList()
            MessagesPageRequests.addChatRequest(
                ChatCreateDTO(
                    chatNameInput.value, usersList
                ), { chat -> }, snackbarChatsHostState
            )

            addChatDialogExpanded.value = false
        }
    }

    fun sendMessage(lazyColumnListState: LazyListState) {
        viewModelScope.launch {
            MessagesPageRequests.sendMessageRequest(
                selectedChat.value,
                MessageDTO(message = message.value),
                { item -> },
                mainSnackbarHostState.value
            )
            lazyColumnListState.scrollToItem(0)
            message.value = ""
        }
    }

    fun deleteChat(chatDTO: ChatDTO?) {
        viewModelScope.launch {
            if (selectedChat.value?.id == chatDTO?.id) {
                selectedChat.value = null
            }
            deleteChatRequest(
                chatDTO, { chat ->
                }, mainSnackbarHostState.value
            )
        }
    }

    fun expandAddChatDialog() {
        addChatDialogExpanded.value = true
    }

    @OptIn(ExperimentalAtomicApi::class)
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

    @OptIn(ExperimentalAtomicApi::class)
    fun subscribeToMessagesUpdates(lazyColumnListState: LazyListState) {
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
                            println("message notice ${stompMessage.data?.id} ${stompMessage.operation}")
                            val first = messages.withIndex().firstOrNull {
                                (stompMessage.data?.id ?: 0) == (it.value?.id ?: 0)
                            }
                            if (sessionId != messagesSessionIdentificator.load()) {
                                return@collect
                            }
                            when (stompMessage.operation) {
                                "ADD" -> {
                                    val index = lazyColumnListState.firstVisibleItemIndex
                                    messages.add(
                                        0, stompMessage.data
                                    )
                                    if (index == 0) {
                                        lazyColumnListState.scrollToItem(
                                            0
                                        )
                                    }
                                }

                                "DELETE" -> {
                                    if (first != null) {
                                        messages.removeAt(
                                            first.index
                                        )
                                    }
                                }

                                "UPDATE" -> {
                                    if (first != null) {
                                        messages[first.index] = stompMessage.data
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