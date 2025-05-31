package ru.ssshteam.potatocoder228.messenger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DisplayMode.Companion.Input
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.use
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.ssshteam.potatocoder228.messenger.dto.ChatCreateDTO
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.dto.OperationDTO
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.deleteChatRequest
import ru.ssshteam.potatocoder228.messenger.requests.MessagesPageRequests.Companion.getChatsRequest
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

enum class UiState {
    Loading, Loaded
}

@OptIn(ExperimentalAtomicApi::class)
var chatsSessionIdentificator: AtomicInt = AtomicInt(0)

@OptIn(ExperimentalAtomicApi::class)
var messagesSessionIdentificator: AtomicInt = AtomicInt(0)

val messagesSessionMutex = Mutex()
val chatsSessionMutex = Mutex()


var messagesCurrentSession: StompSession? = null
var chatsCurrentSession: StompSession? = null

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalAtomicApi::class
)
@Composable
@Preview
fun MessagesPage(navController: NavHostController, onThemeChange: () -> Unit) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "ShhhChat",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.headlineLarge
                        )
                        HorizontalDivider()

                        Text(
                            "Меню",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Комментарии") },
                            selected = false,
                            onClick = { /* Handle click */ },
                            badge = { Text("5") },
                        )
                        NavigationDrawerItem(
                            label = { Text("Сообщения") },
                            selected = false,
                            onClick = { /* Handle click */ },
                            badge = { Text("20") },
                        )
                        NavigationDrawerItem(
                            label = { Text("Избранные сообщения") },
                            selected = false,
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Уведомления") },
                            selected = false,
                            onClick = { /* Handle click */ })

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            "Настройки",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Настройки") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Помощь и обратная связь") },
                            selected = false,
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Outlined.Help, contentDescription = null
                                )
                            },
                            onClick = { /* Handle click */ },
                        )
                        NavigationDrawerItem(label = { Text("Выйти") }, selected = false, icon = {
                            Icon(
                                Icons.AutoMirrored.Rounded.Logout, contentDescription = null
                            )
                        }, onClick = {
                            navController.navigate(PageRoutes.SignInPage.route)
                        })
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }, drawerState = drawerState
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                modifier = if (getPlatform().name == "Java") Modifier.pointerInput(Input) { detectDragGestures { _, _ -> } }
                    .pointerInput(Input) { detectTapGestures { _ -> } } else Modifier,
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }, topBar = {
                    TopAppBar(title = {
                        Text(
                            "ShhhChat", style = MaterialTheme.typography.headlineLarge
                        )
                    }, navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    })
                }) {
                val chats = remember { mutableStateListOf<ChatDTO?>() }
                val messages = remember { mutableStateListOf<MessageDTO?>() }
                val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
                remember(token?.value?.userId) {
                    mutableStateOf(
                        scope.launch {
                            getChatsRequest(
                                snackbarHostState = snackbarHostState, onChatsChange = { chat ->
                                    chats.add(chat)
                                    println("Chat added ${chat.id}")
                                })
                        })
                }
                var selectedChat by remember { mutableStateOf<ChatDTO?>(null) }
                var detailPaneState by remember {
                    mutableStateOf(UiState.Loading)
                }
                ListDetailPaneScaffold(
                    modifier = Modifier.padding(it),
                    directive = scaffoldNavigator.scaffoldDirective,
                    scaffoldState = scaffoldNavigator.scaffoldState,
                    listPane = {
                        AnimatedPane(
                            modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        ) {
                            var expanded by rememberSaveable { mutableStateOf(false) }
                            val textFieldState = rememberTextFieldState()
                            val snackbarChatsHostState = remember { SnackbarHostState() }
                            var addChatDialogExpanded by remember { mutableStateOf(false) }
                            AnimatedVisibility(addChatDialogExpanded) {
                                Dialog(onDismissRequest = {
                                    addChatDialogExpanded = false
                                }) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        shape = RoundedCornerShape(16.dp),
                                    ) {
                                        val chatNameInput = remember { mutableStateOf("") }
                                        var key by remember {
                                            mutableStateOf(0u)
                                        }
                                        val users = remember { mutableStateListOf(key) }
                                        val usersMap by remember { mutableStateOf(mutableMapOf<UInt, String>()) }
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                modifier = Modifier.padding(10.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                text = "Новый чат",
                                                style = MaterialTheme.typography.headlineSmall
                                            )

                                            TextField(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(10.dp, 50.dp, 10.dp, 20.dp),
                                                value = chatNameInput.value,
                                                onValueChange = { chatNameInput.value = it },
                                                placeholder = { Text("Введите название чата!") },
                                                maxLines = 1,
                                                textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                                            )
                                            LazyColumn(modifier = Modifier.height(200.dp)) {
                                                items(
                                                    items = users, key = { user ->
                                                        user.toString()
                                                    }) { item ->
                                                    val usersInput = remember { mutableStateOf("") }
                                                    TextField(
                                                        modifier = Modifier.fillMaxWidth()
                                                            .padding(10.dp),
                                                        value = usersInput.value,
                                                        onValueChange = {
                                                            usersInput.value = it
                                                            usersMap[item] = usersInput.value
                                                        },
                                                        placeholder = { Text("Введите имя пользователя!") },
                                                        maxLines = 1,
                                                    )
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                FloatingActionButton(
                                                    modifier = Modifier.padding(10.dp), onClick = {
                                                        scope.launch {
                                                            users.add(++key)
                                                        }
                                                    }) {
                                                    Icon(
                                                        Icons.Default.Add,
                                                        contentDescription = "Добавить пользователя"
                                                    )
                                                }
                                                FloatingActionButton(
                                                    modifier = Modifier.padding(10.dp), onClick = {
                                                        scope.launch {
                                                            val usersList: MutableList<String> =
                                                                usersMap.values.toMutableList()
                                                            MessagesPageRequests.addChatRequest(
                                                                ChatCreateDTO(
                                                                    chatNameInput.value, usersList
                                                                ),
                                                                { chat -> },
                                                                snackbarChatsHostState
                                                            )

                                                            addChatDialogExpanded = false
                                                        }
                                                    }) {
                                                    Icon(
                                                        Icons.Default.ChatBubble,
                                                        contentDescription = "Создать чат"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Scaffold(
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarChatsHostState)
                                },
                                floatingActionButton = {
                                    FloatingActionButton(
                                        onClick = {
                                            scope.launch {
                                                addChatDialogExpanded = true
                                            }
                                        }) {
                                        Icon(
                                            Icons.Default.Add, contentDescription = "Создать чат"
                                        )
                                    }
                                },
                            ) {
                                Column {
                                    SearchBar(
                                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                                            .semantics { traversalIndex = 0f },
                                        shape = RoundedCornerShape(8.dp),
                                        inputField = {
                                            SearchBarDefaults.InputField(
                                                modifier = Modifier.height(75.dp),
                                                query = textFieldState.text.toString(),
                                                onQueryChange = {
                                                    textFieldState.edit {
                                                        replace(
                                                            0, length, it
                                                        )
                                                    }
                                                },
                                                onSearch = {
                                                    expanded = false
                                                },
                                                expanded = expanded,
                                                onExpandedChange = { expanded = it },
                                                placeholder = { Text("Поиск") },
                                                leadingIcon = {
                                                    if (expanded) {
                                                        IconButton(
                                                            onClick = {
                                                                expanded = !expanded
                                                            }) {
                                                            Icon(
                                                                Icons.AutoMirrored.Default.ArrowBack,
                                                                contentDescription = "Назад"
                                                            )
                                                        }
                                                    } else {
                                                        IconButton(onClick = {
                                                            expanded = !expanded
                                                        }) {
                                                            Icon(
                                                                Icons.Default.Search,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    }
                                                },
                                                trailingIcon = {},

                                                )
                                        },
                                        expanded = expanded,
                                        onExpandedChange = { expanded = it },
                                    ) {

                                    }
                                    remember(token) {
                                        scope.launch {
                                            val identificator =
                                                chatsSessionIdentificator.addAndFetch(1)
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
                                                val jsonStompSession =
                                                    session.withJsonConversions(json)
                                                jsonStompSession.use { s ->
                                                    val subscription: Flow<OperationDTO<ChatDTO>> =
                                                        s.subscribe(
                                                            StompSubscribeHeaders("/topic/user/${token?.value?.userId}/chats"),
                                                            OperationDTO.serializer(ChatDTO.serializer())
                                                        )
                                                    while (identificator == chatsSessionIdentificator.load()) {
                                                        subscription.collect { stompMessage ->
                                                            val first =
                                                                chats.withIndex().firstOrNull {
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
                                                                        chats[first.index] =
                                                                            stompMessage.data
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    LazyColumn {
                                        items(items = chats, key = {
                                            it?.id!!
                                        }) { item ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                                onClick = {
                                                    scope.launch {
                                                        scaffoldNavigator.navigateTo(
                                                            ListDetailPaneScaffoldRole.Detail
                                                        )
                                                        if (item != null) {
                                                            selectedChat = item
                                                        }
                                                        detailPaneState = UiState.Loading
                                                        messages.clear()
                                                        MessagesPageRequests.getChatsMessagesRequest(
                                                            item, snackbarHostState
                                                        ) { message ->
                                                            messages.add(
                                                                message
                                                            )
                                                        }
                                                        detailPaneState = UiState.Loaded
                                                    }
                                                },
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Row(
                                                            modifier = Modifier.padding(6.dp)
                                                                .align(Start)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Person,
                                                                contentDescription = "Person",
                                                                modifier = Modifier.size(60.dp)
                                                                    .clip(CircleShape)
                                                            )
                                                            Column(
                                                                modifier = Modifier.align(
                                                                    CenterVertically
                                                                )
                                                            ) {
                                                                Text(
                                                                    text = item?.name ?: "Unknown",
                                                                    style = MaterialTheme.typography.titleSmall
                                                                )
                                                                Text(
                                                                    text = "Привет, давно тебя не было в уличных гонках!",
                                                                    style = MaterialTheme.typography.bodyMedium,
                                                                    overflow = TextOverflow.Ellipsis,
                                                                    maxLines = 1
                                                                )
                                                            }
                                                        }
                                                    }

                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Row(
                                                            modifier = Modifier.padding(6.dp)
                                                                .align(End)
                                                        ) {
                                                            Badge(
                                                                modifier = Modifier.align(
                                                                    CenterVertically
                                                                ).padding(6.dp),
                                                                containerColor = Color.Red,
                                                                contentColor = Color.White,

                                                                ) {
                                                                Text("1")
                                                            }

                                                            var expandedMenu by remember {
                                                                mutableStateOf(
                                                                    false
                                                                )
                                                            }
                                                            IconButton(
                                                                modifier = Modifier.align(
                                                                    CenterVertically
                                                                ).padding(6.dp), onClick = {
                                                                    expandedMenu = !expandedMenu
                                                                }) {
                                                                Icon(
                                                                    Icons.Default.MoreVert,
                                                                    contentDescription = "Опции"
                                                                )
                                                            }
                                                            DropdownMenu(
                                                                modifier = Modifier.align(
                                                                    CenterVertically
                                                                ),
                                                                expanded = expandedMenu,
                                                                onDismissRequest = {
                                                                    expandedMenu = false
                                                                }) {
                                                                DropdownMenuItem(onClick = {
                                                                    scope.launch {
                                                                        if (selectedChat?.id == item?.id) {
                                                                            selectedChat = null
                                                                        }
                                                                        deleteChatRequest(
                                                                            item, { chat ->
                                                                            }, snackbarHostState
                                                                        )
                                                                    }
                                                                }, text = { Text("Удалить") })
                                                                HorizontalDivider()
                                                                DropdownMenuItem(
                                                                    onClick = { },
                                                                    text = { Text("Настройки") })
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
                    },
                    detailPane = {
                        AnimatedPane(
                            modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        ) {
                            Column {
                                AnimatedVisibility(
                                    selectedChat != null, enter = expandHorizontally(
                                        expandFrom = Start
                                    ), exit = shrinkHorizontally()
                                ) {
                                    val lazyColumnListState = rememberLazyListState()
                                    Scaffold(topBar = {
                                        TopAppBar(title = {
                                            Row {
                                                Text(selectedChat?.name ?: "Unknown")
                                                when (detailPaneState) {
                                                    UiState.Loading -> {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(
                                                                20.dp
                                                            )
                                                        )
                                                    }

                                                    UiState.Loaded -> {

                                                    }
                                                }
                                            }
                                        }, navigationIcon = {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    selectedChat = null
                                                    detailPaneState = UiState.Loading
                                                    scaffoldNavigator.navigateTo(
                                                        ListDetailPaneScaffoldRole.List
                                                    )
                                                }
                                            }) {
                                                Icon(
                                                    Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "Назад"
                                                )
                                            }
                                        }, actions = {
                                            IconButton(onClick = {}) {
                                                Icon(
                                                    Icons.Default.MoreVert,
                                                    contentDescription = "Настройки чата"
                                                )
                                            }
                                        })
                                    }, bottomBar = {
                                        val message = remember { mutableStateOf("") }
                                        TextField(
                                            modifier = Modifier.fillMaxWidth().onPreviewKeyEvent {
                                                when {
                                                    (!it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                                                        scope.launch {
                                                            MessagesPageRequests.sendMessageRequest(
                                                                selectedChat,
                                                                MessageDTO(message = message.value),
                                                                { item -> },
                                                                snackbarHostState
                                                            )
                                                            lazyColumnListState.scrollToItem(0)
                                                            message.value = ""
                                                        }
                                                        true
                                                    }

                                                    (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                                                        message.value += "\n"
                                                        true
                                                    }

                                                    else -> false
                                                }
                                            },
                                            value = message.value,
                                            onValueChange = { message.value = it },
                                            placeholder = { Text("Введите сообщение!") },
                                            leadingIcon = {
                                                IconButton(onClick = {}) {
                                                    Icon(
                                                        Icons.Default.AttachFile,
                                                        contentDescription = "Прикрепить документ"
                                                    )
                                                }
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    scope.launch {
                                                        MessagesPageRequests.sendMessageRequest(
                                                            selectedChat,
                                                            MessageDTO(message = message.value),
                                                            { item -> },
                                                            snackbarHostState
                                                        )
                                                        lazyColumnListState.scrollToItem(0)
                                                        message.value = ""
                                                    }
                                                }) {
                                                    Icon(
                                                        Icons.Default.ChatBubble,
                                                        contentDescription = "Отправить"
                                                    )
                                                }
                                            },
                                            maxLines = 3,
                                        )
                                    }) {
                                        remember(selectedChat?.id) {
                                            scope.launch {
                                                val sessionId =
                                                    messagesSessionIdentificator.addAndFetch(1)
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
                                                    val jsonStompSession =
                                                        session.withJsonConversions(json)
                                                    jsonStompSession.use { s ->
                                                        val subscription: Flow<OperationDTO<MessageDTO>> =
                                                            s.subscribe(
                                                                StompSubscribeHeaders("/topic/chat/${selectedChat?.id}/messages"),
                                                                OperationDTO.serializer(
                                                                    MessageDTO.serializer()
                                                                )
                                                            )
                                                        while (sessionId == messagesSessionIdentificator.load()) {
                                                            subscription.collect { stompMessage ->
                                                                println("message notice ${stompMessage.data?.id} ${stompMessage.operation}")
                                                                val first = messages.withIndex()
                                                                    .firstOrNull {
                                                                        (stompMessage.data?.id
                                                                            ?: 0) == (it.value?.id
                                                                            ?: 0)
                                                                    }
                                                                if (sessionId != messagesSessionIdentificator.load()) {
                                                                    return@collect
                                                                }
                                                                when (stompMessage.operation) {
                                                                    "ADD" -> {
                                                                        val index =
                                                                            lazyColumnListState.firstVisibleItemIndex
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
                                                                            messages[first.index] =
                                                                                stompMessage.data
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        LazyColumn(
                                            modifier = Modifier.padding(it).fillMaxHeight(),
                                            reverseLayout = true,
                                            state = lazyColumnListState
                                        ) {
                                            items(
                                                items = messages,
                                                key = { message -> message?.id ?: 0 }) { item ->
                                                Card(
                                                    modifier = Modifier.fillMaxWidth()
                                                        .padding(8.dp),
                                                    onClick = {
                                                        scope.launch {
                                                            scaffoldNavigator.navigateTo(
                                                                ListDetailPaneScaffoldRole.Detail
                                                            )
                                                        }
                                                    },
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(6.dp)
                                                            .fillMaxWidth()
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    6.dp
                                                                ).align(Start)
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Person,
                                                                    contentDescription = "Person",
                                                                    modifier = Modifier.size(
                                                                        60.dp
                                                                    ).clip(CircleShape)
                                                                )
                                                                Column(
                                                                    modifier = Modifier.align(
                                                                        CenterVertically
                                                                    )
                                                                ) {
                                                                    Text(
                                                                        text = item?.sender
                                                                            ?: "Unknown",
                                                                        fontSize = 20.sp
                                                                    )
                                                                    if (item != null) {
                                                                        Text(
                                                                            text = item.message,
                                                                            fontSize = 11.sp,
                                                                            overflow = TextOverflow.Ellipsis,
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Row(
                                                                modifier = Modifier.padding(
                                                                    6.dp
                                                                ).align(End)
                                                            ) {
                                                                var expandedMenu by remember {
                                                                    mutableStateOf(
                                                                        false
                                                                    )
                                                                }
                                                                IconButton(
                                                                    modifier = Modifier.align(
                                                                        CenterVertically
                                                                    ).padding(6.dp), onClick = {
                                                                        expandedMenu = !expandedMenu
                                                                    }) {
                                                                    Icon(
                                                                        Icons.Default.MoreVert,
                                                                        contentDescription = "Настройки"
                                                                    )
                                                                }
                                                                DropdownMenu(
                                                                    modifier = Modifier.align(
                                                                        CenterVertically
                                                                    ),
                                                                    expanded = expandedMenu,
                                                                    onDismissRequest = {
                                                                        expandedMenu = false
                                                                    }) {
                                                                    DropdownMenuItem(
                                                                        onClick = { },
                                                                        text = { Text("Переслать") })
                                                                    DropdownMenuItem(
                                                                        onClick = { },
                                                                        text = { Text("Удалить") })
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
                        }
                    })
            }
        }

    }
}
