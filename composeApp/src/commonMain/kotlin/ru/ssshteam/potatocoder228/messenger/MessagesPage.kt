package ru.ssshteam.potatocoder228.messenger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.ReadMore
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.format.format
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.viewmodels.MessagesViewModel
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi


enum class UiState {
    Loading, Loaded
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun MessagesPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    val scope = rememberCoroutineScope()
    Row {
        androidx.compose.animation.AnimatedVisibility(
            viewModel.navRailVisible.value,
            enter = slideInHorizontally(),
            exit = shrinkHorizontally()
        ) {
            navRail(navController)
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = viewModel.mainSnackbarHostState.value)
            }, topBar = {
                TopAppBar(title = {
                    Text(
                        "ShhhChat", style = MaterialTheme.typography.headlineLarge
                    )
                }, navigationIcon = {
                    androidx.compose.animation.AnimatedVisibility(
                        !viewModel.navRailVisible.value,
                        enter = slideInHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        IconButton(onClick = {
                            viewModel.navRailVisible.value = true
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    }
                })
            }) {
            val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
            remember(token?.value?.userId) {
                mutableStateOf(
                    viewModel.loadChats()
                )
            }
            ListDetailPaneScaffold(
                modifier = Modifier.padding(it),
                directive = scaffoldNavigator.scaffoldDirective,
                scaffoldState = scaffoldNavigator.scaffoldState,
                listPane = {
                    if (viewModel.fromExtraToDetail.value) {
                        scope.launch {
                            viewModel.fromExtraToDetail.value = false
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    }
                    AnimatedPane(
                        modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        ChatsPane(scaffoldNavigator)
                    }
                },
                detailPane = {
                    if (viewModel.fromDetailToList.value) {
                        scope.launch {
                            viewModel.fromDetailToList.value = false
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.List)
                        }
                    }
                    AnimatedPane(
                        modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        listDetailsContent(scaffoldNavigator)
                    }
                }, extraPane = {
                    AnimatedPane(
                        modifier = Modifier.fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        AnimatedVisibility(
                            viewModel.selectedMsg.value != null, enter = fadeIn(), exit = fadeOut()
                        ) {
                            Scaffold(topBar = {
                                TopAppBar(title = {
                                    Text(
                                        "Обсуждение"
                                    )
                                }, navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            viewModel.fromExtraToDetail.value = true
                                            scaffoldNavigator.navigateTo(
                                                ListDetailPaneScaffoldRole.List
                                            )
                                            viewModel.selectedMsg.value = null
                                        }
                                    }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Назад"
                                        )
                                    }
                                })
                            }, bottomBar = {
                                TextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = "Введите сообщение!",
                                    onValueChange = {},
                                )
                            }) {
                                Scaffold(
                                    modifier = Modifier.padding(it),
                                    topBar = {
                                        Column {
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(5.dp),
                                                onClick = {}
                                            ) {
                                                if (viewModel.chatBoxModifier.value == null) {
                                                    Modifier.padding(2.dp).fillMaxSize().also {
                                                        viewModel.chatBoxModifier.value = it
                                                    }
                                                }
                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = "Person",
                                                        modifier = Modifier.size(
                                                            60.dp
                                                        ).clip(CircleShape).align(TopStart)
                                                    )

                                                    if (viewModel.msgSenderTextModifier.value == null) {
                                                        Modifier.align(TopStart)
                                                            .offset { IntOffset(60, 0) }
                                                            .also {
                                                                viewModel.msgSenderTextModifier.value =
                                                                    it
                                                            }
                                                    }
                                                    msgSenderText(viewModel.selectedMsg.value?.sender)

                                                    if (viewModel.msgTextModifier.value == null) {
                                                        Modifier.align(
                                                            CenterStart
                                                        ).offset { IntOffset(60, 0) }.padding(
                                                            0.dp, 25.dp, 100.dp, 0.dp
                                                        ).also {
                                                            viewModel.msgTextModifier.value = it
                                                        }
                                                    }

                                                    msgText(viewModel.selectedMsg.value?.message)

                                                    if (viewModel.msgSettingsModifier.value == null) {
                                                        Modifier.align(
                                                            CenterEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsModifier.value = it
                                                        }
                                                    }

                                                    if (viewModel.msgSettingsDropdownMenuModifier.value == null) {
                                                        Modifier.align(
                                                            TopEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsDropdownMenuModifier.value =
                                                                it
                                                        }
                                                    }
                                                    msgSettings(message = null)
                                                }
                                            }
                                            HorizontalDivider(
                                                modifier = Modifier.padding(
                                                    0.dp,
                                                    10.dp,
                                                    0.dp,
                                                    10.dp
                                                ), thickness = 2.dp
                                            )
                                        }
                                    }) {
                                    LazyColumn(modifier = Modifier.padding(it).fillMaxHeight()) {
                                        item {
                                            ElevatedCard(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(5.dp),
                                                onClick = {}
                                            ) {
                                                if (viewModel.chatBoxModifier.value == null) {
                                                    Modifier.padding(2.dp).fillMaxSize().also {
                                                        viewModel.chatBoxModifier.value = it
                                                    }
                                                }
                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = "Person",
                                                        modifier = Modifier.size(
                                                            60.dp
                                                        ).clip(CircleShape).align(TopStart)
                                                    )

                                                    if (viewModel.msgSenderTextModifier.value == null) {
                                                        Modifier.align(TopStart)
                                                            .offset { IntOffset(60, 0) }
                                                            .also {
                                                                viewModel.msgSenderTextModifier.value =
                                                                    it
                                                            }
                                                    }
                                                    msgSenderText(viewModel.selectedMsg.value?.sender)

                                                    if (viewModel.msgTextModifier.value == null) {
                                                        Modifier.align(
                                                            CenterStart
                                                        ).offset { IntOffset(60, 0) }.padding(
                                                            0.dp, 25.dp, 100.dp, 0.dp
                                                        ).also {
                                                            viewModel.msgTextModifier.value = it
                                                        }
                                                    }

                                                    msgText("Сообщение в макете для теста")

                                                    if (viewModel.msgSettingsModifier.value == null) {
                                                        Modifier.align(
                                                            CenterEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsModifier.value = it
                                                        }
                                                    }

                                                    if (viewModel.msgSettingsDropdownMenuModifier.value == null) {
                                                        Modifier.align(
                                                            TopEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsDropdownMenuModifier.value =
                                                                it
                                                        }
                                                    }
                                                    msgSettings(message = null)
                                                }
                                            }
                                        }
                                        item {
                                            ElevatedCard(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(5.dp),
                                                onClick = {}
                                            ) {
                                                if (viewModel.chatBoxModifier.value == null) {
                                                    Modifier.padding(2.dp).fillMaxSize().also {
                                                        viewModel.chatBoxModifier.value = it
                                                    }
                                                }
                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = "Person",
                                                        modifier = Modifier.size(
                                                            60.dp
                                                        ).clip(CircleShape).align(TopStart)
                                                    )

                                                    if (viewModel.msgSenderTextModifier.value == null) {
                                                        Modifier.align(TopStart)
                                                            .offset { IntOffset(60, 0) }
                                                            .also {
                                                                viewModel.msgSenderTextModifier.value =
                                                                    it
                                                            }
                                                    }
                                                    msgSenderText(viewModel.selectedMsg.value?.sender)

                                                    if (viewModel.msgTextModifier.value == null) {
                                                        Modifier.align(
                                                            CenterStart
                                                        ).offset { IntOffset(60, 0) }.padding(
                                                            0.dp, 25.dp, 100.dp, 0.dp
                                                        ).also {
                                                            viewModel.msgTextModifier.value = it
                                                        }
                                                    }

                                                    msgText("Сообщение в макете для теста")

                                                    if (viewModel.msgSettingsModifier.value == null) {
                                                        Modifier.align(
                                                            CenterEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsModifier.value = it
                                                        }
                                                    }

                                                    if (viewModel.msgSettingsDropdownMenuModifier.value == null) {
                                                        Modifier.align(
                                                            TopEnd
                                                        ).padding(6.dp).also {
                                                            viewModel.msgSettingsDropdownMenuModifier.value =
                                                                it
                                                        }
                                                    }
                                                    msgSettings(message = null)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ChatsPane(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    AnimatedVisibility(viewModel.addChatDialogExpanded.value) {
        chatsAddNewChatDialog()
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarChatsHostState)
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    viewModel.expandAddChatDialog()
                }) {
                Icon(
                    Icons.Default.Add, contentDescription = "Создать чат"
                )
            }
        },
    ) {
        Column {
            DockedSearchBar(
                modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp)
                    .semantics { traversalIndex = 0f },
                shape = RoundedCornerShape(8.dp),
                inputField = {
                    chatsSearchBarInput()
                },
                expanded = viewModel.expanded.value,
                onExpandedChange = { viewModel.expanded.value = it },
            ) {

            }
            rememberSaveable(token) {
                viewModel.subscribeToUpdates()
                viewModel.subscribeToNotifications()
            }
            chatsList(scaffoldNavigator)
        }
    }
}

@Composable
expect fun EmojiPicker(
    modifier: Modifier, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
)

@Composable
fun chatsAddNewChatDialog(
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    Dialog(onDismissRequest = {
        viewModel.addChatDialogExpanded.value = false
    }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(10.dp).align(CenterHorizontally),
                    text = "Новый чат",
                    style = MaterialTheme.typography.headlineSmall
                )

                TextField(
                    modifier = Modifier.fillMaxWidth().padding(10.dp, 50.dp, 10.dp, 20.dp),
                    value = viewModel.chatNameInput.value,
                    onValueChange = { viewModel.chatNameInput.value = it },
                    placeholder = { Text("Введите название чата!") },
                    maxLines = 1,
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                )
                LazyColumn(modifier = Modifier.height(200.dp)) {
                    items(
                        items = viewModel.usersMap, key = { id ->
                            id.value.first.toString()
                        }) { item ->
                        val usersInput = remember { mutableStateOf("") }
                        TextField(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            value = usersInput.value,
                            onValueChange = {
                                usersInput.value = it
                                viewModel.usersMap[item.value.first].value =
                                    Pair(item.value.first, usersInput.value)
                            },
                            placeholder = { Text("Введите имя пользователя!") },
                            maxLines = 1,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(10.dp), onClick = {
                            viewModel.usersMap.add(mutableStateOf(Pair(++viewModel.key.value, "")))
                        }) {
                        Icon(
                            Icons.Default.Add, contentDescription = "Добавить пользователя"
                        )
                    }
                    FloatingActionButton(
                        modifier = Modifier.padding(10.dp), onClick = {
                            viewModel.addChat()
                        }) {
                        Icon(
                            Icons.Default.ChatBubble, contentDescription = "Создать чат"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun chatsSearchBarInput(
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    val textFieldState = rememberTextFieldState()
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
            viewModel.expanded.value = false
        },
        expanded = viewModel.expanded.value,
        onExpandedChange = { viewModel.expanded.value = it },
        placeholder = { Text("Поиск") },
        leadingIcon = {
            if (viewModel.expanded.value) {
                IconButton(
                    onClick = {
                        viewModel.expanded.value = !viewModel.expanded.value
                    }) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Назад"
                    )
                }
            } else {
                IconButton(onClick = {
                    viewModel.expanded.value = !viewModel.expanded.value
                }) {
                    Icon(
                        Icons.Default.Search, contentDescription = null
                    )
                }
            }
        },
        trailingIcon = {},
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun chatsList(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    val scope = rememberCoroutineScope()
    LazyColumn {
        items(items = viewModel.chats, key = {
            it?.id!!
        }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        scaffoldNavigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
                    }
                    viewModel.showChatMessages(item)
                },
                shape = RoundedCornerShape(5.dp),
            ) {
                if (viewModel.chatBoxModifier.value == null) {
                    Modifier.padding(2.dp).fillMaxSize().also {
                        viewModel.chatBoxModifier.value = it
                    }
                }
                chatBox(item)
            }
        }
    }
}

@Composable
fun chatBox(
    chat: ChatDTO?, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.chatBoxModifier.value?.let {
        Box(
            modifier = Modifier.padding(2.dp).fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Person",
                modifier = Modifier.size(
                    45.dp
                ).clip(CircleShape).align(TopStart)
            )

            Text(
                modifier = Modifier.align(TopStart).padding(0.dp, 5.dp, 150.dp, 0.dp)
                    .offset(60.dp, 0.dp),
                text = chat?.name ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if (viewModel.chatNameTextModifier.value == null) {
                Modifier.align(CenterStart).offset(60.dp, 0.dp)
                    .padding(0.dp, 20.dp, 150.dp, 0.dp).also {
                        viewModel.chatNameTextModifier.value = it
                    }
            }

            chatNameText(chat)

            if (viewModel.chatBadgeCounterModifier.value == null) {
                Modifier.align(
                    CenterEnd
                ).padding(6.dp).offset((-60).dp, 0.dp).also {
                    viewModel.chatBadgeCounterModifier.value = it
                }
            }
            if (viewModel.unreadedCountMap[chat?.id] != null && viewModel.unreadedCountMap[chat?.id]!! > 0) {
                msgsCounterBadge(viewModel.unreadedCountMap[chat?.id].toString())
            }
            var menuExpanded by remember { mutableStateOf(false) }

            IconButton(
                modifier = Modifier.align(
                    CenterEnd
                ).padding(6.dp), onClick = {
                    menuExpanded = !menuExpanded
                }) {
                Icon(
                    Icons.Default.MoreVert, contentDescription = "Опции"
                )
                if (viewModel.chatSettingsDropdownMenuModifier.value == null) {
                    Modifier.align(
                        TopEnd
                    ).padding(6.dp).also {
                        viewModel.chatSettingsDropdownMenuModifier.value = it
                    }
                }
                chatSettingsDropdownMenu(
                    chat, menuExpanded, { value -> menuExpanded = value })
            }
        }
    }
}

@Composable
fun msgsCounterBadge(
    count: String,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.chatBadgeCounterModifier.value?.let {
        Badge(
            modifier = it,
            containerColor = Color.Red,
            contentColor = Color.White
        ) {
            Text(count)
        }
    }
}

@Composable
fun chatNameText(
    chat: ChatDTO?, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.chatNameTextModifier.value?.let {
        Text(
            modifier = it,
            text = "Привет, давно тебя не было в уличных гонках!",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun chatSettingsDropdownMenu(
    chat: ChatDTO?,
    isMenuExpanded: Boolean,
    onMenuExpand: (Boolean) -> Unit,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.chatSettingsDropdownMenuModifier.value?.let {
        DropdownMenu(
            modifier = it, expanded = isMenuExpanded, onDismissRequest = {
                onMenuExpand(false)
            }) {
            DropdownMenuItem(onClick = {
                viewModel.deleteChat(chat)
            }, text = { Text("Удалить") })
            HorizontalDivider()
            DropdownMenuItem(onClick = { }, text = { Text("Настройки") })
        }
    }
}

@Composable
fun navRail(
    navController: NavHostController,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    NavigationRail {
        Column(modifier = Modifier.offset(0.dp, 10.dp)) {
            NavigationRailItem(selected = false, onClick = {
                viewModel.navRailVisible.value = false
            }, icon = {
                Icon(Icons.Default.Menu, contentDescription = "Меню")

            }, label = { Text("Скрыть") })
            NavigationRailItem(selected = false, onClick = {}, icon = {
                BadgedBox(
                    badge = {
                        Badge()
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }, label = { Text("Уведомления") })
            NavigationRailItem(selected = false, onClick = {}, icon = {
                BadgedBox(
                    badge = {
                        Badge()
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Message,
                        contentDescription = "Messages"
                    )
                }
            }, label = { Text("Сообщения") })
            NavigationRailItem(selected = false, onClick = {}, icon = {
                BadgedBox(
                    badge = {
                        Badge()
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReadMore,
                        contentDescription = "Threads"
                    )
                }
            }, label = { Text("Обсуждения") })
            NavigationRailItem(selected = false, onClick = {}, icon = {
                BadgedBox(
                    badge = {
                        Badge()
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = "Settings"
                    )
                }
            }, label = { Text("Настройки") })
            NavigationRailItem(selected = false, onClick = {}, icon = {
                BadgedBox(
                    badge = {
                        Badge()
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Help,
                        contentDescription = "Feedback"
                    )
                }
            }, label = { Text("Помощь") })
            NavigationRailItem(selected = false, onClick = {
                navController.navigate(PageRoutes.SignInPage.route)
            }, icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout, contentDescription = "Logout"
                )
            }, label = { Text("Выйти") })
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun listDetailsContent(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    AnimatedVisibility(
        viewModel.selectedChat.value != null, enter = fadeIn(), exit = fadeOut()
    ) {
        val lazyColumnListState = rememberLazyListState()
        Scaffold(topBar = {
            msgsScaffoldTopBar(scaffoldNavigator)
        }, bottomBar = {
            msgsScaffoldBottomContent(lazyColumnListState)
        }) {

            if (viewModel.msgsColumnModifier.value == null) {
                Modifier.padding(it).fillMaxHeight().also {
                    viewModel.msgsColumnModifier.value = it
                }
            }
            msgsColumn(lazyColumnListState, scaffoldNavigator)
        }
    }
}

@Composable
fun msgsScaffoldBottomContent(
    lazyColumnListState: LazyListState,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    Row {
        IconButton(
            modifier = Modifier.background(Color.Transparent).align(
                CenterVertically
            ), onClick = {
                println("Choose file")
                val fileName: String? = fileChooser?.selectFile()
                if (fileName != null) {
                    viewModel.message.value += fileName
                }

            }) {
            Icon(
                Icons.Default.AttachFile, contentDescription = "Прикрепить документ"
            )
        }
        TextField(
            modifier = Modifier.weight(1f).onPreviewKeyEvent {
                when {
                    (!it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                        if (viewModel.editingMode.value) {
                            viewModel.updateMessage(lazyColumnListState)
                        } else {
                            viewModel.sendMessage(
                                lazyColumnListState
                            )
                        }
                        true
                    }

                    (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                        viewModel.message.value += "\n"
                        true
                    }

                    else -> false
                }
            },
            value = viewModel.message.value,
            onValueChange = { viewModel.message.value = it },
            placeholder = { Text("Введите сообщение!") },
            maxLines = 3,
        )
        IconButton(
            modifier = Modifier.background(Color.Transparent).align(
                CenterVertically
            ), onClick = {
                viewModel.emojiSelector.value = !viewModel.emojiSelector.value
            }) {
            Icon(
                Icons.Default.AddReaction, contentDescription = "Меню смайликов"
            )
            DropdownMenu(
                containerColor = Color.Transparent,
                expanded = viewModel.emojiSelector.value,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                onDismissRequest = {
                    viewModel.emojiSelector.value = false
                }) {
                EmojiPicker(Modifier)
            }
        }
        IconButton(
            modifier = Modifier.background(Color.Transparent).align(
                CenterVertically
            ), onClick = {
                if (viewModel.editingMode.value) {
                    viewModel.updateMessage(lazyColumnListState)
                } else {
                    viewModel.sendMessage(
                        lazyColumnListState
                    )
                }
            }) {
            Icon(
                Icons.Default.ChatBubble, contentDescription = "Отправить"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun msgsScaffoldTopBar(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {

    val scope = rememberCoroutineScope()
    TopAppBar(title = {
        Row {
            Text(
                viewModel.selectedChat.value?.name ?: "Загрузка..."
            )
            when (viewModel.detailPaneState.value) {
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
            viewModel.selectedMsg.value = null
            viewModel.fromDetailToList.value = true
            viewModel.navigateToChats(scope, scaffoldNavigator)
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад"
            )
        }
    }, actions = {
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "Настройки чата"
            )
        }
    })
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun msgsColumn(
    lazyColumnListState: LazyListState,
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgsColumnModifier.value?.let {
        val scope = rememberCoroutineScope()
        remember(viewModel.selectedChat.value?.id) {
            viewModel.subscribeToMessagesUpdates(lazyColumnListState)
        }
        LazyColumn(
            modifier = it, reverseLayout = true, state = lazyColumnListState
        ) {
            items(
                items = viewModel.messages, key = { message -> message?.id ?: 0 }) { item ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            viewModel.selectedMsg.value = item
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        }
                    },
                    shape = RoundedCornerShape(5.dp),
                ) {
                    if (viewModel.msgBoxModifier.value == null) {
                        Modifier.padding(2.dp).fillMaxSize().also {
                            viewModel.msgBoxModifier.value = it
                        }
                    }
                    msgBox(item)
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun msgBox(
    message: MessageDTO?, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgBoxModifier.value?.let {
        Box(
            modifier = it
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                modifier = Modifier.size(
                    60.dp
                ).clip(CircleShape).align(TopStart)
            )

            if (viewModel.msgSenderTextModifier.value == null) {
                Modifier.align(TopStart).offset(60.dp, 0.dp).also {
                    viewModel.msgSenderTextModifier.value = it
                }
            }

            message?.sendAt?.toInstant(UtcOffset.ZERO)
                ?.minus(message.sendAt.toInstant(TimeZone.currentSystemDefault()))
            msgSenderText(message?.sender)
            Text(
                modifier = Modifier.align(TopStart).offset(130.dp, 5.dp).width(80.dp),
                text = DateTimeComponents.Format {
                    hour(); char(':'); minute()
                    char(' ')
                    offsetHours()
                }.format {
                    setTime(
                        message?.sendAt?.toInstant(UtcOffset.ZERO)
                            ?.toLocalDateTime(TimeZone.currentSystemDefault())?.time!!
                    )
                    setOffset(
                        UtcOffset(
                            hours = message.sendAt.toInstant(UtcOffset.ZERO)
                                .minus(message.sendAt.toInstant(TimeZone.currentSystemDefault()))
                                .toInt(DurationUnit.HOURS)
                        )
                    )
                },
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                modifier = Modifier.align(TopStart).offset(200.dp, 5.dp),
                text = if (message?.edited == null) "" else "(edited)",
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            if (viewModel.msgTextModifier.value == null) {
                Modifier.align(
                    CenterStart
                ).offset(60.dp, 0.dp).padding(
                    0.dp, 25.dp, 100.dp, 0.dp
                ).also {
                    viewModel.msgTextModifier.value = it
                }
            }

            msgText(message?.message)

            if (viewModel.msgSettingsModifier.value == null) {
                Modifier.align(
                    CenterEnd
                ).padding(6.dp).also {
                    viewModel.msgSettingsModifier.value = it
                }
            }

            if (viewModel.msgSettingsDropdownMenuModifier.value == null) {
                Modifier.align(
                    TopEnd
                ).padding(6.dp).also {
                    viewModel.msgSettingsDropdownMenuModifier.value = it
                }
            }
            msgSettings(message)
        }
    }
}

@Composable
fun msgSenderText(
    sender: String?, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgSenderTextModifier.value?.let {
        Text(
            modifier = it,
            text = sender ?: "Unknown",
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun msgText(
    message: String?, viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgTextModifier.value?.let {
        Text(
            modifier = it,
            text = message ?: "",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun msgSettings(
    message: MessageDTO?,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgSettingsModifier.value?.let {
        val msgMenuExpanded = remember { mutableStateOf(false) }
        IconButton(
            modifier = it, onClick = {
                msgMenuExpanded.value = !msgMenuExpanded.value
            }) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "Настройки"
            )
            msgSettingsDropdownMenu(
                message,
                msgMenuExpanded.value, { value -> msgMenuExpanded.value = value })
        }
    }
}


@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
@Composable
fun msgSettingsDropdownMenu(
    message: MessageDTO?,
    isMenuExpanded: Boolean,
    onMenuExpand: (Boolean) -> Unit,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    viewModel.msgSettingsDropdownMenuModifier.value?.let {
        DropdownMenu(
            modifier = it, expanded = isMenuExpanded, onDismissRequest = {
                onMenuExpand(false)
            }) {
            DropdownMenuItem(onClick = { }, text = { Text("Переслать") })
            DropdownMenuItem(onClick = {
                if (message != null) {
                    viewModel.msgDTO.value.id = message.id
                    viewModel.msgDTO.value.sendAt = message.sendAt
                    viewModel.msgDTO.value.repliedToId = message.repliedToId
                    viewModel.msgDTO.value.threadParentMsgId = message.threadParentMsgId
                    viewModel.message.value = message.message
                    viewModel.msgDTO.value.edited = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                    viewModel.editingMode.value = true
                }
            }, text = { Text("Редактировать") })
            DropdownMenuItem(onClick = {
                if (message != null) {
                    viewModel.msgDTO.value.id = message.id
                    viewModel.msgDTO.value.sendAt = message.sendAt
                    viewModel.msgDTO.value.repliedToId = message.repliedToId
                    viewModel.msgDTO.value.threadParentMsgId = message.threadParentMsgId
                }
                viewModel.deleteMessage()
            }, text = { Text("Удалить") })
        }
    }
}