package ru.ssshteam.potatocoder228.messenger

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.ssshteam.potatocoder228.messenger.viewmodels.GlobalViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalUuidApi::class
)
@Composable
fun SettingsPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: GlobalViewModel = androidx.lifecycle.viewmodel.compose.viewModel { GlobalViewModel() }
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
                        modifier = Modifier.preferredWidth(700.dp).fillMaxWidth(),
                        enterTransition = fadeIn(),
                        exitTransition = fadeOut()
                    ) {
                        SettingsListPane(scaffoldNavigator, navController)
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
                        SettingsPane(scaffoldNavigator, navController)
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsListPane(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = androidx.lifecycle.viewmodel.compose.viewModel { GlobalViewModel() }
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarListHostState)
        }
    ) {
        Column {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsPane(
    scaffoldNavigator: ThreePaneScaffoldNavigator<String>,
    navController: NavHostController,
    viewModel: GlobalViewModel = androidx.lifecycle.viewmodel.compose.viewModel { GlobalViewModel() }
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarListHostState)
        },
        bottomBar = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = {
                    }) {
                    Text("Сохранить")
                }
                TextButton(
                    onClick = {
                    }) {
                    Text("Отмена")
                }
            }
        },
    ) {
        Column {}
    }
}