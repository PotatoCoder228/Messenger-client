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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.ssshteam.potatocoder228.messenger.viewmodels.AppViewModel
import ru.ssshteam.potatocoder228.messenger.viewmodels.MessagesViewModel
import kotlin.concurrent.atomics.ExperimentalAtomicApi

// TODO contentType = { it.type }

enum class UiState {
    Loading, Loaded
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ChatsPane(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    onThemeChange: () -> Unit,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    val scope = rememberCoroutineScope()
    val textFieldState = rememberTextFieldState()
    AnimatedVisibility(viewModel.addChatDialogExpanded.value) {
        Dialog(onDismissRequest = {
            viewModel.addChatDialogExpanded.value = false
        }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
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
                        value = viewModel.chatNameInput.value,
                        onValueChange = { viewModel.chatNameInput.value = it },
                        placeholder = { Text("Введите название чата!") },
                        maxLines = 1,
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                    )
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(
                            items = viewModel.users, key = { user ->
                                user.toString()
                            }) { item ->
                            val usersInput = remember { mutableStateOf("") }
                            TextField(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(10.dp),
                                value = usersInput.value,
                                onValueChange = {
                                    usersInput.value = it
                                    viewModel.usersMap.value[item] = usersInput.value
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
                                viewModel.users.add(++viewModel.key.value)
                            }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Добавить пользователя"
                            )
                        }
                        FloatingActionButton(
                            modifier = Modifier.padding(10.dp), onClick = {
                                viewModel.addChat()
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
            SnackbarHost(hostState = viewModel.snackbarChatsHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
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
                                        Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Назад"
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    viewModel.expanded.value = !viewModel.expanded.value
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
                expanded = viewModel.expanded.value,
                onExpandedChange = { viewModel.expanded.value = it },
            ) {

            }
            remember(token) {
                viewModel.subscribeToUpdates()
            }
            LazyColumn {
                items(items = viewModel.chats, key = {
                    it?.id!!
                }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        onClick = {
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail
                                )
                            }
                            viewModel.showChatMessages(item)
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
                                            viewModel.deleteChat(item)
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

@Composable
fun MessagesPane(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() },
    content: @Composable () -> Unit
) {

}

@Composable
fun NavigatorSheet(navController: NavHostController, content: @Composable () -> Unit) {
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
}

fun Navigator(content: @Composable () -> Unit) {

}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalAtomicApi::class
)
@Composable
fun MessagesPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    appViewModel: AppViewModel = viewModel { AppViewModel() },
    viewModel: MessagesViewModel = viewModel { MessagesViewModel() }
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerContent = {
                NavigatorSheet(navController) {}
            }, drawerState = drawerState
        ) {
            Scaffold(
                modifier = if (getPlatform().name == "Java") Modifier.pointerInput(Input) { detectDragGestures { _, _ -> } }
                    .pointerInput(Input) { detectTapGestures { _ -> } } else Modifier,
                snackbarHost = {
                    SnackbarHost(hostState = viewModel.mainSnackbarHostState.value)
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
                        AnimatedPane(
                            modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        ) {
                            ChatsPane(scaffoldNavigator, onThemeChange)
                        }
                    },
                    detailPane = {
                        AnimatedPane(
                            modifier = Modifier.preferredWidth(500.dp).fillMaxWidth(),
                        ) {
                            val scope = rememberCoroutineScope()
                            Column {
                                AnimatedVisibility(
                                    viewModel.selectedChat.value != null,
                                    enter = expandHorizontally(
                                        expandFrom = Start
                                    ),
                                    exit = shrinkHorizontally()
                                ) {
                                    val lazyColumnListState = rememberLazyListState()
                                    Scaffold(topBar = {
                                        TopAppBar(title = {
                                            Row {
                                                Text(
                                                    viewModel.selectedChat.value?.name ?: "Unknown"
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
                                                viewModel.navigateToChats(scope, scaffoldNavigator)
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
                                        TextField(
                                            modifier = Modifier.fillMaxWidth().onPreviewKeyEvent {
                                                when {
                                                    (!it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                                                        viewModel.sendMessage(lazyColumnListState)
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
                                                    viewModel.sendMessage(lazyColumnListState)
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
                                        remember(viewModel.selectedChat.value?.id) {
                                            viewModel.subscribeToMessagesUpdates(lazyColumnListState)
                                        }
                                        LazyColumn(
                                            modifier = Modifier.padding(it).fillMaxHeight(),
                                            reverseLayout = true,
                                            state = lazyColumnListState
                                        ) {
                                            items(
                                                items = viewModel.messages,
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
