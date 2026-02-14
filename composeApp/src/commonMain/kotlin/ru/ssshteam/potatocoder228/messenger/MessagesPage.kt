package ru.ssshteam.potatocoder228.messenger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.ReadMore
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Css
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.Html
import androidx.compose.material.icons.filled.Javascript
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RawOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled._3mp
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.format.format
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import ru.ssshteam.potatocoder228.messenger.dto.ChatDTO
import ru.ssshteam.potatocoder228.messenger.dto.MessageDTO
import ru.ssshteam.potatocoder228.messenger.internal.File
import ru.ssshteam.potatocoder228.messenger.requests.getMessageFileRequest
import ru.ssshteam.potatocoder228.messenger.viewmodels.GlobalViewModel
import kotlin.math.pow
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

enum class UiState {
    Loading, Loaded
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalUuidApi::class
)
@Composable
fun MessagesPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    var listWidth by remember { mutableStateOf(400f) }
    var extraWidth by remember { mutableStateOf(300f) }
    val displayMetrics = LocalWindowInfo.current.containerSize

    // Width and height of screen
    val displayWidth = displayMetrics.width

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
                TopAppBar(modifier = Modifier.shadow(4.dp), title = {
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
                    viewModel.loadChats(navController)
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
                            viewModel.selectedMsg.value = null
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    }
                    AnimatedPane(
                        modifier = Modifier.preferredWidth((listWidth).dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        Row {
                            ChatsPane(scaffoldNavigator, navController)
                            VerticalDragHandle(
                                modifier =
                                    Modifier.fillMaxHeight().pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            listWidth =
                                                (listWidth + (0.5 * dragAmount.x).toInt()).coerceIn(
                                                    300.toFloat(),
                                                    (displayWidth * 0.8).toFloat(),
                                                )
                                        }
                                    }
                            )
                        }
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
                        modifier = Modifier.preferredWidth(300.dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        Row {
                            listDetailsContent(scaffoldNavigator, navController)
                            if (viewModel.selectedMsg.value != null) {
                                VerticalDragHandle(
                                    modifier =
                                        Modifier.fillMaxHeight().pointerInput(Unit) {
                                            detectDragGestures { change, dragAmount ->
                                                change.consume()
                                                extraWidth =
                                                    (extraWidth - (0.5 * dragAmount.x).toInt()).coerceIn(
                                                        300.toFloat(),
                                                        (displayWidth * 0.8).toFloat(),
                                                    )
                                            }
                                        }
                                )
                            }
                        }
                    }
                }, extraPane = {
                    AnimatedPane(
                        modifier = Modifier.preferredWidth(extraWidth.dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        AnimatedVisibility(
                            viewModel.selectedMsg.value != null, enter = fadeIn(), exit = fadeOut()
                        ) {
                            val lazyColumnListState = rememberLazyListState()
                            Scaffold(topBar = {
                                TopAppBar(title = {
                                    Text(
                                        "Обсуждение"
                                    )
                                }, navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            viewModel.fromExtraToDetail.value = true
                                            viewModel.selectedMsg.value = null
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
                                })
                            }, bottomBar = {
                                var zoomed by remember { mutableStateOf(false) }
                                var page by remember { mutableIntStateOf(0) }
                                val iconsMapper = remember {
                                    mutableMapOf(
                                        "html" to Icons.Default.Html,
                                        "mp3" to Icons.Default._3mp,
                                        "js" to Icons.Default.Javascript,
                                        "css" to Icons.Default.Css,
                                        "raw" to Icons.Default.RawOn,
                                        "gif" to Icons.Default.Gif,
                                        "pdf" to Icons.Default.Book
                                    )
                                }
                                val selectedFiles =
                                    remember { mutableStateListOf<File>() }
                                AnimatedVisibility(zoomed) {
                                    BasicAlertDialog(
                                        onDismissRequest = { zoomed = false },
                                        properties = DialogProperties(
                                            usePlatformDefaultWidth = false
                                        ),
                                        modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f)
                                            .imePadding()
                                    ) {
                                        val pagerState = rememberPagerState(
                                            initialPage = page,
                                            pageCount = {
                                                selectedFiles.size
                                            })
                                        Column(
                                            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                            horizontalAlignment = CenterHorizontally
                                        ) {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(0.9f)
                                                    .padding(10.dp),
                                                text = selectedFiles[pagerState.currentPage].filename,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                textAlign = TextAlign.Center
                                            )
                                            Box(
                                                modifier = Modifier.fillMaxWidth(0.9f)
                                                    .fillMaxHeight(0.9f),
                                                contentAlignment = Center
                                            ) {
                                                HorizontalPager(
                                                    state = pagerState,
                                                    modifier = Modifier.align(Center)
                                                ) { page ->
                                                    if (selectedFiles[page].extension != "png" && selectedFiles[page].extension != "jpg" && selectedFiles[page].extension != "jpeg") {
                                                        Icon(
                                                            imageVector = when (val icon =
                                                                iconsMapper[selectedFiles[page].extension]) {
                                                                null -> Icons.Default.FilePresent
                                                                else -> icon
                                                            },
                                                            contentDescription = "Document",
                                                            modifier = Modifier
                                                                .clip(CircleShape),
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                    } else {
                                                        AsyncImage(
                                                            selectedFiles[page].uri,
                                                            null,
                                                            modifier = Modifier.align(Center)
                                                                .fillMaxWidth().fillMaxHeight(),
                                                            alignment = Center
                                                        )
                                                    }
                                                }
                                                IconButton(
                                                    modifier = Modifier.align(CenterStart)
                                                        .size(75.dp), onClick = {
                                                        scope.launch {
                                                            pagerState.scrollToPage(if (pagerState.currentPage - 1 >= 0) pagerState.currentPage - 1 else pagerState.pageCount - 1)
                                                        }
                                                    }) {
                                                    Icon(
                                                        Icons.AutoMirrored.Default.ArrowLeft,
                                                        contentDescription = "Назад"
                                                    )
                                                }
                                                IconButton(
                                                    modifier = Modifier.align(CenterEnd)
                                                        .size(75.dp), onClick = {
                                                        scope.launch {
                                                            pagerState.scrollToPage(if (pagerState.currentPage + 1 < pagerState.pageCount) pagerState.currentPage + 1 else 0)
                                                        }
                                                    }) {
                                                    Icon(
                                                        Icons.AutoMirrored.Default.ArrowRight,
                                                        contentDescription = "Вперёд"
                                                    )
                                                }
                                                Row(
                                                    Modifier
                                                        .wrapContentWidth()
                                                        .padding(bottom = 8.dp).align(BottomCenter),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    repeat(pagerState.pageCount) { iteration ->
                                                        val color =
                                                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                                        Box(
                                                            modifier = Modifier
                                                                .padding(2.dp)
                                                                .clip(CircleShape)
                                                                .background(color)
                                                                .size(16.dp).clickable(
                                                                    onClick = {
                                                                        scope.launch {
                                                                            pagerState.scrollToPage(
                                                                                iteration
                                                                            )
                                                                        }
                                                                    }
                                                                )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Column {
                                    Row {
                                        LazyRow {
                                            items(selectedFiles) { file ->
                                                ElevatedCard(
                                                    modifier = Modifier
                                                        .wrapContentWidth().wrapContentHeight()
                                                ) {
                                                    BoxWithConstraints(
                                                        modifier = Modifier
                                                            .width(200.dp)
                                                    ) {
                                                        if (file.extension != "png" && file.extension != "jpg" && file.extension != "jpeg") {
                                                            Icon(
                                                                imageVector = when (val icon =
                                                                    iconsMapper[file.extension]) {
                                                                    null -> Icons.Default.FilePresent
                                                                    else -> icon
                                                                },
                                                                contentDescription = "Document",
                                                                modifier = Modifier.size(
                                                                    50.dp
                                                                ).clip(CircleShape)
                                                                    .align(TopStart),
                                                                tint = MaterialTheme.colorScheme.primary
                                                            )
                                                        } else {
                                                            IconButton(
                                                                modifier = Modifier.size(50.dp),
                                                                onClick = {
                                                                    zoomed = true
                                                                    page =
                                                                        selectedFiles.indexOf(file)
                                                                }) {
                                                                AsyncImage(
                                                                    file.uri,
                                                                    null,
                                                                    modifier = Modifier.size(
                                                                        50.dp
                                                                    ).clip(CircleShape)
                                                                        .align(TopStart),
                                                                    contentScale = ContentScale.Crop
                                                                )
                                                            }
                                                        }
                                                        Text(
                                                            modifier = Modifier.align(TopStart)
                                                                .offset(55.dp, 3.dp)
                                                                .width(120.dp),
                                                            text = file.filename,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            overflow = TextOverflow.Ellipsis,
                                                            maxLines = 1,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )

                                                        Text(
                                                            modifier = Modifier.align(CenterStart)
                                                                .offset(55.dp, 10.dp)
                                                                .width(120.dp),
                                                            text = file.extension,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            overflow = TextOverflow.Ellipsis,
                                                            maxLines = 1,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                        IconButton(
                                                            modifier = Modifier.align(TopEnd)
                                                                .size(20.dp).padding(2.dp),
                                                            onClick = {
                                                                selectedFiles.remove(file)
                                                            }
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Close,
                                                                contentDescription = "Открепить документ"
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    androidx.compose.animation.AnimatedVisibility(viewModel.threadEditingMode.value) {
                                        ElevatedCard {
                                            Box(Modifier.fillMaxWidth().height(50.dp)) {
                                                Icon(
                                                    Icons.Filled.Edit,
                                                    contentDescription = "Edit",
                                                    modifier = Modifier.align(CenterStart)
                                                        .padding(15.dp, 0.dp).size(25.dp)
                                                )
                                                Text(
                                                    "Редактирование",
                                                    modifier = Modifier.align(TopStart)
                                                        .offset(50.dp, 0.dp),
                                                    style = MaterialTheme.typography.titleSmall
                                                )
                                                Text(
                                                    viewModel.editingThreadMsgOrigText.value,
                                                    modifier = Modifier.align(CenterStart)
                                                        .basicMarquee().offset(50.dp, 0.dp)
                                                        .width(500.dp),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    maxLines = 1
                                                )
                                                IconButton(
                                                    modifier = Modifier.align(CenterEnd)
                                                        .size(25.dp), onClick = {
                                                        viewModel.threadEditingMode.value = false
                                                    }) {
                                                    Icon(
                                                        Icons.Filled.Cancel,
                                                        contentDescription = "Cancel editing"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Row {
                                        IconButton(
                                            modifier = Modifier.background(Color.Transparent).align(
                                                CenterVertically
                                            ), onClick = {
                                                println("Choose file")
                                                scope.launch {
                                                    selectedFiles.addAll(fileChooser!!.selectFile())
                                                }
                                            }) {
                                            Icon(
                                                Icons.Default.AttachFile,
                                                contentDescription = "Прикрепить документ"
                                            )
                                        }
                                        TextField(
                                            modifier = Modifier.weight(1f).onPreviewKeyEvent {
                                                when {
                                                    (!it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                                                        if (viewModel.threadEditingMode.value) {
                                                            viewModel.updateThreadMessage(
                                                                navController
                                                            )
                                                        } else {
                                                            viewModel.sendThreadMessage(
                                                                lazyColumnListState,
                                                                selectedFiles,
                                                                navController
                                                            )
                                                        }
                                                        true
                                                    }

                                                    (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                                                        viewModel.threadMessage.value += "\n"
                                                        true
                                                    }

                                                    else -> false
                                                }
                                            },
                                            value = viewModel.threadMessage.value,
                                            onValueChange = { viewModel.threadMessage.value = it },
                                            placeholder = { Text("Введите сообщение!") },
                                            maxLines = 3,
                                        )
                                        val emojiSelector = remember { mutableStateOf(false) }
                                        IconButton(
                                            modifier = Modifier.background(Color.Transparent).align(
                                                CenterVertically
                                            ), onClick = {
                                                emojiSelector.value =
                                                    !emojiSelector.value
                                            }) {
                                            Icon(
                                                Icons.Default.AddReaction,
                                                contentDescription = "Меню смайликов"
                                            )
                                            DropdownMenu(
                                                containerColor = Color.Transparent,
                                                expanded = emojiSelector.value,
                                                tonalElevation = 0.dp,
                                                shadowElevation = 0.dp,
                                                onDismissRequest = {
                                                    emojiSelector.value = false
                                                }) {
                                                EmojiPicker(
                                                    Modifier,
                                                    { emoji -> viewModel.threadMessage.value += emoji })
                                            }
                                        }
                                        IconButton(
                                            modifier = Modifier.background(Color.Transparent).align(
                                                CenterVertically
                                            ), onClick = {
                                                if (viewModel.threadEditingMode.value) {
                                                    viewModel.updateThreadMessage(navController)
                                                } else {
                                                    viewModel.sendThreadMessage(
                                                        lazyColumnListState,
                                                        selectedFiles,
                                                        navController
                                                    )
                                                }
                                            }) {
                                            Icon(
                                                Icons.Default.ChatBubble,
                                                contentDescription = "Отправить"
                                            )
                                        }
                                    }
                                }
                            }) {
                                Scaffold(
                                    modifier = Modifier.padding(it),
                                    topBar = {
                                        Column {
                                            val msgMenuExpanded =
                                                remember { mutableStateOf(false) }
                                            Box(Modifier.fillMaxWidth().padding(5.dp, 5.dp)) {
                                                ElevatedCard(
                                                    modifier = Modifier,
                                                    onClick = {
                                                        scope.launch {
                                                            msgMenuExpanded.value = true
                                                        }
                                                    },
                                                    colors =
                                                        if (viewModel.selectedMsg.value?.senderId == currentUserId) {
                                                            CardColors(
                                                                MaterialTheme.colorScheme.secondaryContainer,
                                                                MaterialTheme.colorScheme.onSecondaryContainer,
                                                                MaterialTheme.colorScheme.surfaceContainerLowest,
                                                                MaterialTheme.colorScheme.onSurface
                                                            )
                                                        } else {
                                                            CardColors(
                                                                MaterialTheme.colorScheme.surfaceContainer,
                                                                MaterialTheme.colorScheme.onSurface,
                                                                MaterialTheme.colorScheme.surfaceContainerLowest,
                                                                MaterialTheme.colorScheme.onSurface
                                                            )
                                                        },
                                                    shape = RoundedCornerShape(10.dp),
                                                ) {
                                                    if (viewModel.msgBoxModifier.value == null) {
                                                        Modifier.also {
                                                            viewModel.msgBoxModifier.value = it
                                                        }
                                                    }
                                                    msgBox(
                                                        viewModel.selectedMsg.value,
                                                        msgMenuExpanded.value,
                                                        { value -> msgMenuExpanded.value = value },
                                                        navController
                                                    )
                                                    if ((viewModel.selectedMsg.value?.filesUrls?.size
                                                            ?: 0) > 0
                                                    ) {
                                                        LazyColumn(
                                                            modifier = Modifier.heightIn(
                                                                50.dp,
                                                                600.dp
                                                            ).padding(0.dp, 5.dp)
                                                                .width(
                                                                    (((viewModel.selectedMsg.value?.message?.length
                                                                        ?: 0) + 10) * 16).dp
                                                                )
                                                        ) {
                                                            items(
                                                                items = viewModel.selectedMsg.value?.filesUrls
                                                                    ?: arrayOf(),
                                                                key = { file ->
                                                                    file.id
                                                                }) { msg ->
                                                                val fileLoading = remember {
                                                                    mutableStateOf(0)
                                                                }
                                                                Row {
                                                                    IconButton(
                                                                        onClick = {
                                                                            scope.launch {
                                                                                val path =
                                                                                    fileChooser!!.selectDownloadingFilepath(
                                                                                        msg.name
                                                                                    )
                                                                                val source =
                                                                                    if (getPlatform().name != "Android") SystemFileSystem.sink(
                                                                                        Path(path)
                                                                                    ) else null
                                                                                getMessageFileRequest(
                                                                                    msg.url,
                                                                                    source,
                                                                                    {
                                                                                        fileLoading.value =
                                                                                            1
                                                                                    },
                                                                                    {
                                                                                        fileLoading.value =
                                                                                            2
                                                                                    },
                                                                                    viewModel.mainSnackbarHostState.value,
                                                                                    navController,
                                                                                    path
                                                                                )
                                                                            }
                                                                        },
                                                                        colors = IconButtonColors(
                                                                            MaterialTheme.colorScheme.secondaryContainer,
                                                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                                                            MaterialTheme.colorScheme.surfaceContainer,
                                                                            MaterialTheme.colorScheme.onSurface
                                                                        )
                                                                    ) {
                                                                        val headers =
                                                                            NetworkHeaders.Builder()
                                                                                .set(
                                                                                    "Authorization",
                                                                                    "Bearer ${token?.value?.token}"
                                                                                )
                                                                                .set(
                                                                                    "Cache-Control",
                                                                                    "private"
                                                                                )
                                                                                .set(
                                                                                    "Content-Type",
                                                                                    "application/json"
                                                                                )
                                                                                .build()
                                                                        val request =
                                                                            ImageRequest.Builder(
                                                                                LocalPlatformContext.current
                                                                            )
                                                                                .data("$httpHost${msg.url}")
                                                                                .httpHeaders(headers)
                                                                                .build()
                                                                        when (fileLoading.value) {
                                                                            0 -> {
                                                                                if (msg.contentType == "jpg" || msg.contentType == "png") {
                                                                                    AsyncImage(
                                                                                        request,
                                                                                        null,
                                                                                        contentScale = ContentScale.Crop
                                                                                    )
                                                                                } else {
                                                                                    Icon(
                                                                                        Icons.Default.Download,
                                                                                        contentDescription = "Download file",
                                                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                                    )
                                                                                }
                                                                            }

                                                                            1 -> {
                                                                                CircularProgressIndicator()
                                                                            }

                                                                            2 -> {
                                                                                Icon(
                                                                                    Icons.Default.DownloadDone,
                                                                                    contentDescription = "Downloaded file",
                                                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                    Column {
                                                                        Text(
                                                                            msg.name,
                                                                            modifier = Modifier.width(
                                                                                ((((viewModel.selectedMsg.value?.message?.length
                                                                                    ?: 0) + 10) * 16)).dp
                                                                            )
                                                                                .padding(2.dp),
                                                                            style = MaterialTheme.typography.bodySmall,
                                                                            maxLines = 1,
                                                                            overflow = TextOverflow.Ellipsis
                                                                        )
                                                                        Row(Modifier.width(65.dp)) {
                                                                            Text(
                                                                                msg.contentType,
                                                                                modifier = Modifier
                                                                                    .padding(2.dp),
                                                                                style = MaterialTheme.typography.labelSmall,
                                                                                maxLines = 1
                                                                            )
                                                                            Text(
                                                                                formatSize(msg.size),
                                                                                modifier = Modifier.width(
                                                                                    (((viewModel.selectedMsg.value?.message?.length
                                                                                        ?: 0) + 10) * 16).dp
                                                                                )
                                                                                    .padding(2.dp),
                                                                                style = MaterialTheme.typography.labelSmall,
                                                                                maxLines = 1
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
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
                                    LazyColumn(
                                        modifier = Modifier.padding(it).fillMaxHeight(),
                                        state = lazyColumnListState
                                    ) {
                                        items(
                                            items = viewModel.threadMessages, key = { message ->
                                                message?.id ?: 0
                                            }) { item ->
                                            val msgMenuExpanded =
                                                remember { mutableStateOf(false) }
                                            ElevatedCard(
                                                modifier = Modifier.widthIn(250.dp, 600.dp)
                                                    .padding(5.dp, 5.dp),
                                                onClick = {
                                                    scope.launch {
                                                        msgMenuExpanded.value = true
                                                    }
                                                },
                                                colors =
                                                    if (item?.senderId == currentUserId) {
                                                        CardColors(
                                                            MaterialTheme.colorScheme.secondaryContainer,
                                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                                            MaterialTheme.colorScheme.surfaceContainerLowest,
                                                            MaterialTheme.colorScheme.onSurface
                                                        )
                                                    } else {
                                                        CardColors(
                                                            MaterialTheme.colorScheme.surfaceContainer,
                                                            MaterialTheme.colorScheme.onSurface,
                                                            MaterialTheme.colorScheme.surfaceContainerLowest,
                                                            MaterialTheme.colorScheme.onSurface
                                                        )
                                                    },
                                                shape = RoundedCornerShape(10.dp),
                                            ) {
                                                if (viewModel.msgBoxModifier.value == null) {
                                                    Modifier.widthIn(250.dp, 600.dp)
                                                        .padding(2.dp).also {
                                                            viewModel.msgBoxModifier.value = it
                                                        }
                                                }
                                                msgBox(
                                                    item,
                                                    msgMenuExpanded.value,
                                                    { value -> msgMenuExpanded.value = value },
                                                    navController
                                                )

                                                if ((item?.filesUrls?.size
                                                        ?: 0) > 0
                                                ) {
                                                    LazyColumn(
                                                        modifier = Modifier.heightIn(
                                                            50.dp,
                                                            600.dp
                                                        ).padding(0.dp, 5.dp)
                                                            .width(
                                                                (((item?.message?.length
                                                                    ?: 0) + 10) * 16).dp
                                                            )
                                                    ) {
                                                        items(
                                                            items = item?.filesUrls!!,
                                                            key = { file ->
                                                                file.id
                                                            }) { msg ->
                                                            val fileLoading = remember {
                                                                mutableStateOf(0)
                                                            }
                                                            Row {
                                                                IconButton(
                                                                    onClick = {
                                                                        scope.launch {
                                                                            val path =
                                                                                fileChooser?.selectDownloadingFilepath(
                                                                                    msg.name
                                                                                ) ?: ""
                                                                            val source =
                                                                                if (getPlatform().name != "Android") SystemFileSystem.sink(
                                                                                    Path(path)
                                                                                ) else null
                                                                            getMessageFileRequest(
                                                                                msg.url,
                                                                                source,
                                                                                {
                                                                                    fileLoading.value =
                                                                                        1
                                                                                },
                                                                                {
                                                                                    fileLoading.value =
                                                                                        2
                                                                                },
                                                                                viewModel.mainSnackbarHostState.value,
                                                                                navController,
                                                                                path
                                                                            )
                                                                        }
                                                                    },
                                                                    colors = IconButtonColors(
                                                                        MaterialTheme.colorScheme.secondaryContainer,
                                                                        MaterialTheme.colorScheme.onSecondaryContainer,
                                                                        MaterialTheme.colorScheme.surfaceContainer,
                                                                        MaterialTheme.colorScheme.onSurface
                                                                    )
                                                                ) {
                                                                    val headers =
                                                                        NetworkHeaders.Builder()
                                                                            .set(
                                                                                "Authorization",
                                                                                "Bearer ${token?.value?.token}"
                                                                            )
                                                                            .set(
                                                                                "Cache-Control",
                                                                                "private"
                                                                            )
                                                                            .set(
                                                                                "Content-Type",
                                                                                "application/json"
                                                                            )
                                                                            .build()
                                                                    val request =
                                                                        ImageRequest.Builder(
                                                                            LocalPlatformContext.current
                                                                        )
                                                                            .data("$httpHost${msg.url}")
                                                                            .httpHeaders(headers)
                                                                            .build()
                                                                    when (fileLoading.value) {
                                                                        0 -> {
                                                                            if (msg.contentType == "jpg" || msg.contentType == "png") {
                                                                                AsyncImage(
                                                                                    request,
                                                                                    null,
                                                                                    contentScale = ContentScale.Crop
                                                                                )
                                                                            } else {
                                                                                Icon(
                                                                                    Icons.Default.Download,
                                                                                    contentDescription = "Download file",
                                                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                                )
                                                                            }
                                                                        }

                                                                        1 -> {
                                                                            CircularProgressIndicator()
                                                                        }

                                                                        2 -> {
                                                                            Icon(
                                                                                Icons.Default.DownloadDone,
                                                                                contentDescription = "Downloaded file",
                                                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Column {
                                                                    Text(
                                                                        msg.name,
                                                                        modifier = Modifier.width(
                                                                            (((item.message.length + 10) * 16)).dp
                                                                        )
                                                                            .padding(2.dp),
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        maxLines = 1,
                                                                        overflow = TextOverflow.Ellipsis
                                                                    )
                                                                    Row(Modifier.width(65.dp)) {
                                                                        Text(
                                                                            msg.contentType,
                                                                            modifier = Modifier
                                                                                .padding(2.dp),
                                                                            style = MaterialTheme.typography.labelSmall,
                                                                            maxLines = 1
                                                                        )
                                                                        Text(
                                                                            formatSize(msg.size),
                                                                            modifier = Modifier.width(
                                                                                ((item.message.length + 10) * 16).dp
                                                                            )
                                                                                .padding(2.dp),
                                                                            style = MaterialTheme.typography.labelSmall,
                                                                            maxLines = 1
                                                                        )
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
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    AnimatedVisibility(viewModel.addChatDialogExpanded.value) {
        chatsAddNewChatDialog(navController)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarListHostState)
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
            LaunchedEffect(key1 = Unit) {
                viewModel.expanded.value = false
            }
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { viewModel.expanded.value = it.isFocused }
                    .focusable(),
                shape = RoundedCornerShape(25.dp),
                inputField = {
                    chatsSearchBarInput(navController)
                },
                expanded = viewModel.expanded.value,
                onExpandedChange = { viewModel.expanded.value = it },
            ) {
                searchChatsList(scaffoldNavigator, navController)
            }
            remember(token) {
                viewModel.subscribeToUpdates()
                viewModel.subscribeToNotifications(navController)
            }
            chatsList(scaffoldNavigator, navController)
        }
    }
}

@Composable
expect fun EmojiPicker(
    modifier: Modifier, onEmojiSelect: (String) -> Unit,
)

@Composable
fun chatsAddNewChatDialog(
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
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
                            viewModel.addChat(navController)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatsSearchBarInput(
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    val textFieldState = rememberTextFieldState()

    SearchBarDefaults.InputField(
        modifier = Modifier.height(60.dp),
        query = textFieldState.text.toString(),
        onQueryChange = {
            textFieldState.edit {
                replace(
                    0, length, it
                )
            }
        },
        onSearch = {
            viewModel.expanded.value = true
            // get list of chats
        },
        enabled = true,
        expanded = viewModel.expanded.value,
        onExpandedChange = {
            viewModel.expanded.value = it
        },
        placeholder = { Text("Поиск") },
        leadingIcon = {
            if (viewModel.expanded.value == true) {
                IconButton(
                    onClick = {
                        viewModel.expanded.value = false
                    }) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Назад"
                    )
                }
            } else {
                IconButton(onClick = {
                    viewModel.expanded.value = true
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalUuidApi::class)
@Composable
fun searchChatsList(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() },
) {
    val lazyColumnListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LazyColumn(state = lazyColumnListState) {
        items(items = viewModel.objectsSearchResult, key = {
            it?.id!!
        }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
//                        viewModel.selectedChat.value = item
//                        viewModel.showChatMessages(item, navController)
//                        scaffoldNavigator.navigateTo(
//                            ListDetailPaneScaffoldRole.Detail
//                        )
                    }
                },
                colors = CardColors(
                    if (viewModel.selectedChat.value?.id == item?.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    MaterialTheme.colorScheme.secondaryFixedDim,
                    MaterialTheme.colorScheme.onSecondaryFixed
                ),
                shape = RoundedCornerShape(5.dp),
            ) {
                if (viewModel.chatBoxModifier.value == null) {
                    Modifier.padding(2.dp).fillMaxSize().also {
                        viewModel.chatBoxModifier.value = it
                    }
                }
//                chatBox(item, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalUuidApi::class)
@Composable
fun chatsList(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() },
) {
    val lazyColumnListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    fun LazyListState.isScrolledToTheEnd() =
        layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
    if (lazyColumnListState.isScrolledToTheEnd() && !viewModel.chatsListMutex.isLocked) {
        scope.launch {
            viewModel.loadPreviousChats(navController)
            delay(3000L)
        }
    } else if (lazyColumnListState.firstVisibleItemIndex == 0 && !viewModel.chatsListMutex.isLocked) {
        scope.launch {
            viewModel.loadNextChats(navController)
            delay(3000L)
        }
    }

    if (viewModel.chatsListModified.value) {
        scope.launch {
            viewModel.loadChats(navController)
            lazyColumnListState.animateScrollToItem(0)
            viewModel.chatsListModified.value = false
        }
    }
    LazyColumn(state = lazyColumnListState) {
        items(items = viewModel.chats, key = {
            it?.id!!
        }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        viewModel.selectedChat.value = item
                        viewModel.showChatMessages(item, navController)
                        scaffoldNavigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
                    }
                },
                colors = CardColors(
                    if (viewModel.selectedChat.value?.id == item?.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    MaterialTheme.colorScheme.secondaryFixedDim,
                    MaterialTheme.colorScheme.onSecondaryFixed
                ),
                shape = RoundedCornerShape(5.dp),
            ) {
                if (viewModel.chatBoxModifier.value == null) {
                    Modifier.padding(2.dp).fillMaxSize().also {
                        viewModel.chatBoxModifier.value = it
                    }
                }
                chatBox(item, navController)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun chatBox(
    chat: ChatDTO?,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.chatBoxModifier.value?.let {
        Box(
            modifier = Modifier.padding(2.dp).fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                modifier = Modifier.requiredSize(55.dp).padding(5.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .align(TopStart)
            )

            Text(
                modifier = Modifier.align(TopStart).padding(0.dp, 5.dp, 150.dp, 0.dp)
                    .offset(60.dp, 0.dp),
                text = chat?.name ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if (viewModel.chatNameTextModifier.value == null) {
                Modifier.align(CenterStart).offset(60.dp, 0.dp)
                    .padding(0.dp, 20.dp, 150.dp, 0.dp).also {
                        viewModel.chatNameTextModifier.value = it
                    }
            }

            chatNameText(chat, navController)

            if (viewModel.chatBadgeCounterModifier.value == null) {
                Modifier.align(
                    CenterEnd
                ).padding(6.dp).offset((-60).dp, 0.dp).also {
                    viewModel.chatBadgeCounterModifier.value = it
                }
            }
            if (chat?.unreadedMessages!! > 0) {
                msgsCounterBadge(chat.unreadedMessages.toString(), navController)
            }
            var menuExpanded by remember { mutableStateOf(false) }

            Text(
                text = DateTimeComponents.Format {
                    hour(); char(':'); minute()
                }.format {
                    setTime(
                        chat.lastMsgSendAt.toInstant(UtcOffset.ZERO)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).time
                    )
                },
                modifier = Modifier.align(
                    TopEnd
                ).padding(6.dp).offset((-40).dp),
                style = MaterialTheme.typography.labelSmall
            )
            IconButton(
                modifier = Modifier.align(
                    TopEnd
                ).size(30.dp).padding(6.dp).offset((-20).dp), onClick = {
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
                    chat, menuExpanded, { value -> menuExpanded = value }, navController
                )
            }
        }
    }
}

@Composable
fun msgsCounterBadge(
    count: String,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() },
) {
    viewModel.chatBadgeCounterModifier.value?.let {
        Badge(
            modifier = it,
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ) {
            Text(count)
        }
    }
}

@Composable
fun threadMsgsCounterBadge(
    count: String,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.threadBadgeCounterModifier.value?.let {
        Badge(
            modifier = it,
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ) {
            Text(count)
        }
    }
}

@Composable
fun chatNameText(
    chat: ChatDTO?,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.chatNameTextModifier.value?.let {
        var lastMsg by remember(chat) { mutableStateOf("") }
        if (chat?.lastMsgOwner != "") {
            lastMsg = chat?.lastMsgOwner + ": " + chat?.lastMsgData
        }
        Text(
            modifier = it,
            text = lastMsg,
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
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.chatSettingsDropdownMenuModifier.value?.let {
        DropdownMenu(
            modifier = it, expanded = isMenuExpanded, onDismissRequest = {
                onMenuExpand(false)
            }) {
            DropdownMenuItem(onClick = {
                viewModel.deleteChat(chat, navController)
            }, text = { Text("Удалить") })
            HorizontalDivider()
            DropdownMenuItem(onClick = { }, text = { Text("Настройки") })
        }
    }
}

@Composable
fun navRail(
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
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
            NavigationRailItem(selected = false, onClick = {
            }, icon = {
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
            NavigationRailItem(selected = false, onClick = {
                navController.navigate(PageRoutes.SettingsPage.route)
            }, icon = {
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
                datastore?.saveCookie("savePass", "")
                datastore?.saveCookie("token", "")
                datastore?.saveCookie("refreshToken", "")
                datastore?.saveCookie("userId", "")
                navController.navigate(PageRoutes.SignInPage.route)
            }, icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout, contentDescription = "Logout"
                )
            }, label = { Text("Выйти") })
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalTime::class)
@Composable
fun listDetailsContent(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        viewModel.selectedChat.value != null, enter = fadeIn(), exit = fadeOut()
    ) {
        val lazyColumnListState = rememberLazyListState()
        Scaffold(
            topBar = {
                msgsScaffoldTopBar(scaffoldNavigator, navController)
            },
            bottomBar = {
                msgsScaffoldBottomContent(lazyColumnListState, navController)
            },
        ) {
            if (viewModel.msgsColumnModifier.value == null) {
                Modifier.padding(it).background(
                    Color.Transparent
                ).fillMaxWidth().fillMaxHeight().also {
                    viewModel.msgsColumnModifier.value = it
                }
            }
            Box {
                Text(
                    "Welcome to ShhhChat",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.blur(1.dp).padding(it).offset(2.dp).width(200.dp).align(
                        TopStart
                    )
                )
                var time by remember {
                    mutableStateOf(
                        Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                    )
                }
                time = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    buildAnnotatedString {
                        append("System information as of ${time.dayOfWeek.name} ${time.month.name} ${time.day} ${time.hour}:${time.minute}:${time.second}\n")
                        append("Platform ${getPlatform().name}, Client Version: 0.0.1-alpha\n")
                        append(
                            "Beginning of development: 13 Oct 2023\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n"
                        )
                        append("To disable this message please turn off it in the settings\n")
                        append("Your data is always safe with us =)\n")
                        append("Expect more news soon.\n")
                    },
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.blur(1.dp).padding(it).offset(2.dp).align(
                        TopStart
                    ).padding(0.dp, 100.dp, 0.dp, 0.dp)
                )
                fun LazyListState.isScrolledToTheEnd() =
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
                if (lazyColumnListState.isScrolledToTheEnd() && !viewModel.messagesListMutex.isLocked) {
                    scope.launch {
                        viewModel.uploadPreviousChatMessages(
                            viewModel.selectedChat.value,
                            navController
                        )
                    }
                } else if (lazyColumnListState.firstVisibleItemIndex == 0 && !viewModel.messagesListMutex.isLocked) {
                    scope.launch {
                        viewModel.uploadNextChatMessages(
                            viewModel.selectedChat.value,
                            navController
                        )
                    }
                }
                msgsColumn(lazyColumnListState, scaffoldNavigator, navController)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun profilePopup(viewModel: GlobalViewModel = viewModel { GlobalViewModel() }) {
    AnimatedVisibility(viewModel.chatProfileOpened.value) {
        BasicAlertDialog(
            onDismissRequest = { viewModel.chatProfileOpened.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            ),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f)
        ) {
            Column(Modifier.width(400.dp), horizontalAlignment = CenterHorizontally) {
                Card(
                    Modifier.width(400.dp),
                    shape = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                ) {
                    Column(Modifier.width(400.dp), horizontalAlignment = CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person",
                            modifier = Modifier.size(100.dp).padding(10.dp, 40.dp, 10.dp, 20.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )
                        Text("Admins", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "Был в сети 2 часа назад",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Column(
                            Modifier.width(400.dp).padding(0.dp, 100.dp, 0.dp, 0.dp),
                            horizontalAlignment = CenterHorizontally
                        ) {
                            TextButton({}, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                    Text("Участники")
                                    Badge {
                                        Text("3")
                                    }
                                }
                            }
                            TextButton({}, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Админы")
                                    Badge {
                                        Text("1")
                                    }
                                }
                            }
                            TextButton({}, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Права доступа")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun msgsScaffoldBottomContent(
    lazyColumnListState: LazyListState,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    val scope = rememberCoroutineScope()
    val selectedFiles =
        remember { mutableStateListOf<File>() }
    var zoomed by remember { mutableStateOf(false) }
    var page by remember { mutableIntStateOf(0) }
    val iconsMapper = remember {
        mutableMapOf(
            "html" to Icons.Default.Html,
            "mp3" to Icons.Default._3mp,
            "js" to Icons.Default.Javascript,
            "css" to Icons.Default.Css,
            "raw" to Icons.Default.RawOn,
            "gif" to Icons.Default.Gif,
            "pdf" to Icons.Default.Book
        )
    }

    AnimatedVisibility(zoomed) {
        BasicAlertDialog(
            onDismissRequest = { zoomed = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f).imePadding()
        ) {
            val pagerState = rememberPagerState(
                initialPage = page,
                pageCount = {
                    selectedFiles.size
                })
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.9f).padding(10.dp),
                    text = selectedFiles[pagerState.currentPage].filename,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f),
                    contentAlignment = Center
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.align(Center)
                    ) { page ->
                        if (selectedFiles[page].extension != "png" && selectedFiles[page].extension != "jpg" && selectedFiles[page].extension != "jpeg") {
                            Icon(
                                imageVector = when (val icon =
                                    iconsMapper[selectedFiles[page].extension]) {
                                    null -> Icons.Default.FilePresent
                                    else -> icon
                                },
                                contentDescription = "Document",
                                modifier = Modifier
                                    .clip(CircleShape),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            AsyncImage(
                                selectedFiles[page].uri,
                                null,
                                modifier = Modifier.align(Center).fillMaxWidth().fillMaxHeight(),
                                alignment = Center
                            )
                        }
                    }
                    IconButton(modifier = Modifier.align(CenterStart).size(75.dp), onClick = {
                        scope.launch {
                            pagerState.scrollToPage(if (pagerState.currentPage - 1 >= 0) pagerState.currentPage - 1 else pagerState.pageCount - 1)
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowLeft, contentDescription = "Назад"
                        )
                    }
                    IconButton(modifier = Modifier.align(CenterEnd).size(75.dp), onClick = {
                        scope.launch {
                            pagerState.scrollToPage(if (pagerState.currentPage + 1 < pagerState.pageCount) pagerState.currentPage + 1 else 0)
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowRight, contentDescription = "Вперёд"
                        )
                    }
                    Row(
                        Modifier
                            .wrapContentWidth()
                            .padding(bottom = 8.dp).align(BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(16.dp).clickable(
                                        onClick = {
                                            scope.launch {
                                                pagerState.scrollToPage(iteration)
                                            }
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

    Column(Modifier.imePadding()) {
        Row {
            LazyRow {
                items(selectedFiles) { file ->
                    ElevatedCard(
                        modifier = Modifier
                            .wrapContentWidth().wrapContentHeight()
                    ) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .width(200.dp)
                        ) {
                            if (file.extension != "png" && file.extension != "jpg" && file.extension != "jpeg") {
                                Icon(
                                    imageVector = when (val icon = iconsMapper[file.extension]) {
                                        null -> Icons.Default.FilePresent
                                        else -> icon
                                    },
                                    contentDescription = "Document",
                                    modifier = Modifier.size(
                                        50.dp
                                    ).clip(CircleShape)
                                        .align(TopStart), tint = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                IconButton(
                                    modifier = Modifier.size(50.dp),
                                    onClick = {
                                        zoomed = true
                                        page = selectedFiles.indexOf(file)
                                    }) {
                                    AsyncImage(
                                        file.uri, null, modifier = Modifier.size(
                                            50.dp
                                        ).clip(CircleShape)
                                            .align(TopStart),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier.align(TopStart).offset(55.dp, 3.dp)
                                    .width(120.dp),
                                text = file.filename,
                                style = MaterialTheme.typography.labelMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                modifier = Modifier.align(CenterStart).offset(55.dp, 10.dp)
                                    .width(120.dp),
                                text = file.extension,
                                style = MaterialTheme.typography.labelSmall,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                modifier = Modifier.align(TopEnd).size(20.dp).padding(2.dp),
                                onClick = {
                                    selectedFiles.remove(file)
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close, contentDescription = "Открепить документ"
                                )
                            }
                        }
                    }
                }
            }
        }
        androidx.compose.animation.AnimatedVisibility(viewModel.editingMode.value) {
            ElevatedCard {
                Box(Modifier.fillMaxWidth().height(50.dp)) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.align(CenterStart).padding(15.dp, 0.dp).size(25.dp)
                    )
                    Text(
                        "Редактирование",
                        modifier = Modifier.align(TopStart).offset(50.dp, 0.dp),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        viewModel.editingMsgOrigText.value,
                        modifier = Modifier.align(CenterStart).basicMarquee().offset(50.dp, 0.dp)
                            .width(500.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    IconButton(modifier = Modifier.align(CenterEnd).size(25.dp), onClick = {
                        viewModel.editingMode.value = false
                    }) {
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Cancel editing"
                        )
                    }
                }
            }
        }
        Row {
            IconButton(
                modifier = Modifier.background(Color.Transparent).align(
                    CenterVertically
                ), onClick = {
                    println("Choose file")
                    scope.launch {
                        selectedFiles.addAll(fileChooser!!.selectFile())
                    }
                    println(selectedFiles.size)
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
                                viewModel.updateMessage(navController)
                            } else {
                                viewModel.sendMessage(
                                    lazyColumnListState,
                                    selectedFiles,
                                    navController
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
                placeholder = { Text("root@ssshchat:~#") },
                maxLines = 3,
            )
            val emojiSelector = remember { mutableStateOf(false) }
            IconButton(
                modifier = Modifier.background(Color.Transparent).align(
                    CenterVertically
                ), onClick = {
                    emojiSelector.value = !emojiSelector.value
                }) {
                Icon(
                    Icons.Default.AddReaction, contentDescription = "Меню смайликов"
                )
                DropdownMenu(
                    containerColor = Color.Transparent,
                    expanded = emojiSelector.value,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    onDismissRequest = {
                        emojiSelector.value = false
                    }) {
                    EmojiPicker(Modifier, { emoji -> viewModel.message.value += emoji })
                }
            }
            IconButton(
                modifier = Modifier.align(
                    CenterVertically
                ), onClick = {
                    scope.launch {
                        if (viewModel.editingMode.value) {
                            viewModel.updateMessage(navController)
                        } else {
                            viewModel.sendMessage(
                                lazyColumnListState,
                                selectedFiles,
                                navController
                            )
                        }
                    }
                }) {
                Icon(
                    Icons.Default.ChatBubble, contentDescription = "Отправить"
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalUuidApi::class
)
@Composable
fun msgsScaffoldTopBar(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(viewModel.chatProfileOpened.value) {
        profilePopup()
    }
    val tooltipState = rememberTooltipState(initialIsVisible = false, isPersistent = true)
    val chatNameInteractionSource = remember { MutableInteractionSource() }
    val isChatNameHovered by chatNameInteractionSource.collectIsHoveredAsState()
    val tooltipInteractionSource = remember { MutableInteractionSource() }
    val tooltipIsHovered by tooltipInteractionSource.collectIsHoveredAsState()
    if (isChatNameHovered || tooltipIsHovered) {
        scope.launch { tooltipState.show(MutatePriority.PreventUserInput) }
    }
    remember {
        viewModel.showChatProfiles(viewModel.selectedChat.value, navController)
    }
    TopAppBar(
        expandedHeight = 2.dp,
        windowInsets = WindowInsets(),
        modifier = Modifier.clickable {
            viewModel.chatProfileOpened.value = true
        },
        title = {
            Column {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        ) {
                            append((viewModel.selectedChat.value?.name ?: "Загрузка...") + "\n")
                        }
                    },
                    modifier = Modifier
                        .widthIn(100.dp, 200.dp).height(20.dp),
                    maxLines = 1
                )
                TooltipBox(
                    positionProvider = rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                    tooltip = {
                        RichTooltip(
                            modifier = Modifier.hoverable(tooltipInteractionSource),
                            title = {
                                Text(
                                    "Список пользователей",
                                    modifier = Modifier.basicMarquee()
                                )
                            },
                            action = {},
                            // caretSize = DpSize(32.dp, 16.dp)
                        ) {
                            remember {
                                viewModel.showChatProfiles(
                                    viewModel.selectedChat.value,
                                    navController
                                )
                            }
                            if (viewModel.chatProfiles.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.width(150.dp),
                                ) {
                                    items(
                                        items = viewModel.chatProfiles, key = { profile ->
                                            profile?.id ?: 0
                                        }) { profile ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RectangleShape,
                                        ) {
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Column(
                                                    Modifier.align(
                                                        Alignment.Top
                                                    ).weight(0.9f).padding(5.dp)
                                                ) {
                                                    Text(
                                                        profile?.login ?: "Downloading login...",
                                                        modifier = Modifier
                                                            .basicMarquee(),
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        profile?.role ?: "Undefined",
                                                        modifier = Modifier.padding(1.dp),
                                                        style = MaterialTheme.typography.labelSmall
                                                    )
                                                }
                                                println(profile?.role)
                                                Canvas(
                                                    modifier = Modifier.padding(10.dp).align(
                                                        CenterVertically
                                                    )
                                                ) {
                                                    scale(scaleX = 1f, scaleY = 1f) {
                                                        if (profile?.status == "ONLINE") {
                                                            drawCircle(
                                                                Color.Green,
                                                                radius = 4.dp.toPx()
                                                            )
                                                        } else {
                                                            drawCircle(
                                                                Color.Red,
                                                                radius = 4.dp.toPx()
                                                            )
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
                    state = tooltipState
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                                )
                            ) {
                                append("${viewModel.selectedChatMembers.value} участников, ${viewModel.selectedChatMembersOnline.value} в сети")
                            }
                        },
                        modifier = Modifier.hoverable(chatNameInteractionSource)
                            .widthIn(100.dp, 200.dp),
                        maxLines = 1
                    )
                }
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

private var KB = 1024.0f
private var MB = KB.pow(2)
private var GB = KB.pow(3)
private var TB = KB.pow(4)
private var PB = KB.pow(5)
fun formatSize(v: Long): String {
    return if (v < MB) {
        "${(v / KB).fastRoundToInt()} KB"
    } else if (v < GB) {
        "${(v / MB).fastRoundToInt()} MB"
    } else if (v < TB) {
        "${(v / GB).fastRoundToInt()} GB"
    } else if (v < PB) {
        "${(v / TB).fastRoundToInt()} TB"
    } else {
        "$v bytes"
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalUuidApi::class)
@Composable
fun msgsColumn(
    lazyColumnListState: LazyListState,
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.msgsColumnModifier.value?.let {
        val scope = rememberCoroutineScope()
        remember(viewModel.selectedChat.value?.id) {
            viewModel.subscribeToMessagesUpdates(lazyColumnListState, navController)
        }
        LazyColumn(
            modifier = it, reverseLayout = true, state = lazyColumnListState
        ) {
            items(
                items = viewModel.messages, key = { message -> message?.id ?: 0 }) { item ->
                val msgMenuExpanded = remember { mutableStateOf(false) }
                ElevatedCard(
                    modifier = Modifier.navigationBarsPadding(),
                    onClick = {
                        scope.launch {
                            msgMenuExpanded.value = true
                        }
                    },
                    colors =
                        if (item?.senderId == currentUserId) {
                            CardDefaults.cardColors(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                                MaterialTheme.colorScheme.surfaceContainerLowest,
                                MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            CardColors(
                                MaterialTheme.colorScheme.surfaceContainer,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.surfaceContainerLowest,
                                MaterialTheme.colorScheme.onSurface
                            )
                        },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(hoveredElevation = 0.dp)
                ) {
                    if (viewModel.msgBoxModifier.value == null) {
                        Modifier.also {
                            viewModel.msgBoxModifier.value = it
                        }
                    }
                    msgBox(
                        item,
                        msgMenuExpanded.value,
                        { value -> msgMenuExpanded.value = value }, navController
                    )

                    if (item?.filesUrls?.size!! > 0) {
                        LazyColumn(
                            modifier = Modifier.heightIn(50.dp, 600.dp).padding(0.dp, 5.dp)
                                .width(((item.message.length + 10) * 16).dp)
                        ) {
                            items(
                                items = item.filesUrls, key = { file ->
                                    file.id
                                }) { msg ->
                                println(msg.url)
                                var fileLoading = remember {
                                    mutableStateOf(0)
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                val path =
                                                    fileChooser!!.selectDownloadingFilepath(msg.name)
                                                val source =
                                                    if (getPlatform().name != "Android") SystemFileSystem.sink(
                                                        Path(path)
                                                    ) else null
                                                getMessageFileRequest(
                                                    msg.url,
                                                    source,
                                                    { fileLoading.value = 1 },
                                                    { fileLoading.value = 2 },
                                                    viewModel.mainSnackbarHostState.value,
                                                    navController,
                                                    path
                                                )
                                            }
                                        },
                                        colors = IconButtonColors(
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                            MaterialTheme.colorScheme.surfaceContainer,
                                            MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        val headers = NetworkHeaders.Builder()
                                            .set("Authorization", "Bearer ${token?.value?.token}")
                                            .set("Cache-Control", "private")
                                            .set("Content-Type", "application/json")
                                            .build()
                                        val request =
                                            ImageRequest.Builder(LocalPlatformContext.current)
                                                .data("$httpHost${msg.url}")
                                                .httpHeaders(headers)
                                                .build()
                                        when (fileLoading.value) {
                                            0 -> {
                                                if (msg.contentType == "jpg" || msg.contentType == "png") {
                                                    AsyncImage(
                                                        request,
                                                        null,
                                                        contentScale = ContentScale.Crop
                                                    )
                                                } else {
                                                    Icon(
                                                        Icons.Default.Download,
                                                        contentDescription = "Download file",
                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }

                                            1 -> {
                                                CircularProgressIndicator()
                                            }

                                            2 -> {
                                                Icon(
                                                    Icons.Default.DownloadDone,
                                                    contentDescription = "Downloaded file",
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }
                                    }
                                    Column {
                                        Text(
                                            msg.name,
                                            modifier = Modifier.width(((item.message.length + 10) * 16).dp)
                                                .padding(2.dp),
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(Modifier.width(65.dp)) {
                                            Text(
                                                msg.contentType,
                                                modifier = Modifier
                                                    .padding(2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                maxLines = 1
                                            )
                                            Text(
                                                formatSize(msg.size),
                                                modifier = Modifier.width(((item.message.length + 10) * 16).dp)
                                                    .padding(2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row {
                        IconButton(
                            modifier = Modifier.width(IntrinsicSize.Min)
                                .requiredHeight(30.dp),
                            onClick = {
                                //ToDo
                            },
                            colors = IconButtonColors(
                                if (item.senderId == currentUserId) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainer
                                },
                                if (item.senderId == currentUserId) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = "Избранное",
                                modifier = Modifier.padding(0.dp, 5.dp).requiredHeight(20.dp)
                            )

//                            Icon(
//                                Icons.Filled.Favorite,
//                                contentDescription = "Избранное",
//                                tint = Color.Red
//                            )
                        }

                        TextButton(
                            modifier = Modifier.width(IntrinsicSize.Min)
                                .requiredHeight(30.dp),
                            onClick = {
                                scope.launch {
                                    viewModel.selectedMsg.value = item
                                    viewModel.showChatThreadMessages(
                                        viewModel.selectedChat.value,
                                        navController
                                    )
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Extra
                                    )
                                }
                            },
                            contentPadding = PaddingValues(0.dp, 0.dp),
                            colors = ButtonColors(
                                if (item.senderId == currentUserId) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainer
                                },
                                if (item.senderId == currentUserId) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(
                                    Icons.Rounded.Forum,
                                    contentDescription = "Обсуждение",
                                    Modifier.padding(1.dp, 0.dp).requiredHeight(20.dp)
                                )

                                if (viewModel.threadBadgeCounterModifier.value == null) {
                                    Modifier.padding(0.dp, 2.dp).also {
                                        viewModel.threadBadgeCounterModifier.value = it
                                    }
                                }

                                if (item.unreadMessages > 0) {
                                    threadMsgsCounterBadge(item.unreadMessages.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun msgBox(
    message: MessageDTO?, msgMenuExpanded: Boolean,
    onMenuExpand: (Boolean) -> Unit,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.msgBoxModifier.value?.let {
        Box(
            modifier = it.basicMarquee()
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                modifier = Modifier.size(
                    45.dp
                ).padding(5.dp, 3.dp, 0.dp, 10.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .align(TopStart)
            )

            if (viewModel.msgSenderTextModifier.value == null) {
                Modifier.align(TopStart).width(120.dp).basicMarquee().offset(50.dp, 0.dp).also {
                    viewModel.msgSenderTextModifier.value = it
                }
            }
            msgSenderText(message?.sender)
            Text(
                modifier = Modifier.align(TopEnd).offset(0.dp, 4.dp).width(80.dp),
                text = DateTimeComponents.Format {
                    hour(); char(':'); minute()
                    char(' ')
                    offsetHours()
                }.format {
                    setTime(
                        message?.sendAt?.toInstant(UtcOffset.ZERO)
                            ?.toLocalDateTime(TimeZone.currentSystemDefault())?.time
                            ?: Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).time
                    )
                    setOffset(
                        UtcOffset(
                            hours = (message?.sendAt?.toInstant(UtcOffset.ZERO)
                                ?: Clock.System.now())
                                .minus(
                                    message?.sendAt?.toInstant(TimeZone.currentSystemDefault())
                                        ?: Clock.System.now()
                                )
                                .toInt(DurationUnit.HOURS)
                        )
                    )
                },
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            if (message?.edited != null) {
                TooltipBox(
                    modifier = Modifier.align(TopStart).offset(210.dp, 3.dp).padding(2.dp, 0.dp)
                        .size(15.dp),
                    positionProvider = rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above,
                        5.dp
                    ),
                    tooltip = {
                        PlainTooltip(Modifier.offset(150.dp)) { Text("Сообщение отредактировано") }
                    },
                    state = rememberTooltipState()
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edited icon",
                        tint = Color(MaterialTheme.colorScheme.tertiary.value)
                    )
                }
            }

            if (viewModel.msgTextModifier.value == null) {
                Modifier.align(
                    CenterStart
                ).offset(50.dp, 0.dp).padding(
                    0.dp, 25.dp, 100.dp, 0.dp
                ).also {
                    viewModel.msgTextModifier.value = it
                }
            }

            msgText(message?.message)

            if (viewModel.msgSettingsDropdownMenuModifier.value == null) {
                Modifier.align(
                    TopEnd
                ).padding(6.dp).also {
                    viewModel.msgSettingsDropdownMenuModifier.value = it
                }
            }
            msgSettingsDropdownMenu(
                message,
                msgMenuExpanded, onMenuExpand,
                navController
            )
        }
    }
}

@Composable
fun msgSenderText(
    sender: String?, viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.msgSenderTextModifier.value?.let {
        Text(
            modifier = it,
            text = sender ?: "Unknown",
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun msgText(
    message: String?, viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.msgTextModifier.value?.let {
        Text(
            modifier = it.widthIn(100.dp, 250.dp),
            text = message ?: "",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
@Composable
fun msgSettingsDropdownMenu(
    message: MessageDTO?,
    isMenuExpanded: Boolean,
    onMenuExpand: (Boolean) -> Unit,
    navController: NavHostController,
    viewModel: GlobalViewModel = viewModel { GlobalViewModel() }
) {
    viewModel.msgSettingsDropdownMenuModifier.value?.let {
        DropdownMenu(
            modifier = it, expanded = isMenuExpanded, onDismissRequest = {
                onMenuExpand(false)
            }) {
            DropdownMenuItem(onClick = { onMenuExpand(false) }, text = { Text("Переслать") })
            DropdownMenuItem(onClick = {
                if (message != null) {
                    if (message.messageType == "REGULAR") {
                        viewModel.msgDTO.value.id = message.id
                        viewModel.msgDTO.value.sendAt = message.sendAt
                        viewModel.msgDTO.value.repliedToId = message.repliedToId
                        viewModel.msgDTO.value.messageType = message.messageType
                        viewModel.msgDTO.value.sender = message.sender
                        viewModel.msgDTO.value.threadParentMsgId = message.threadParentMsgId
                        viewModel.msgDTO.value.edited =
                            Clock.System.now().toLocalDateTime(TimeZone.UTC)
                        viewModel.message.value = message.message
                        viewModel.editingMsgOrigText.value = message.message
                        viewModel.editingMode.value = true
                    } else if (message.messageType == "THREAD") {
                        viewModel.threadMsgDTO.value.id = message.id
                        viewModel.threadMsgDTO.value.sendAt = message.sendAt
                        viewModel.threadMsgDTO.value.repliedToId = message.repliedToId
                        viewModel.threadMsgDTO.value.messageType = message.messageType
                        viewModel.threadMsgDTO.value.threadParentMsgId = message.threadParentMsgId
                        viewModel.threadMsgDTO.value.edited =
                            Clock.System.now().toLocalDateTime(TimeZone.UTC)
                        viewModel.editingThreadMsgOrigText.value = message.message
                        viewModel.threadMessage.value = message.message
                        viewModel.threadEditingMode.value = true
                    }
                }
                onMenuExpand(false)
            }, text = { Text("Редактировать") })
            DropdownMenuItem(onClick = {
                if (message != null) {
                    viewModel.msgDTO.value.id = message.id
                    viewModel.msgDTO.value.sendAt = message.sendAt
                    viewModel.msgDTO.value.repliedToId = message.repliedToId
                    viewModel.msgDTO.value.messageType = message.messageType
                    viewModel.msgDTO.value.threadParentMsgId = message.threadParentMsgId
                }
                viewModel.deleteMessage(navController)
                onMenuExpand(false)
            }, text = { Text("Удалить") })
        }
    }
}