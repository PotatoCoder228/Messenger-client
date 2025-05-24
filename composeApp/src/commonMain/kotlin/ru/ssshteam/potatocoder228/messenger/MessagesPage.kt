package ru.ssshteam.potatocoder228.messenger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun MessagesPage(navController: NavHostController) {
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
                            style = MaterialTheme.typography.titleLarge
                        )
                        HorizontalDivider()

                        Text(
                            "Menu",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Threads") },
                            selected = false,
                            onClick = { /* Handle click */ },
                            badge = { Text("5") },
                        )
                        NavigationDrawerItem(
                            label = { Text("Messages") },
                            selected = false,
                            onClick = { /* Handle click */ },
                            badge = { Text("20") },
                        )
                        NavigationDrawerItem(
                            label = { Text("Saved Messages") },
                            selected = false,
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Notifications") },
                            selected = false,
                            onClick = { /* Handle click */ })

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            "Settings",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Help and feedback") },
                            selected = false,
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Outlined.Help, contentDescription = null
                                )
                            },
                            onClick = { /* Handle click */ },
                        )
                        NavigationDrawerItem(label = { Text("Logout") }, selected = false, icon = {
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
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("ShhhChat") }, navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    })
                }) {
                val coroutineScope = rememberCoroutineScope()
                val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
                val items = listOf("Item 1", "Item 2", "Item 3")
                val selectedItem = scaffoldNavigator.currentDestination?.contentKey

                ListDetailPaneScaffold(
                    modifier = Modifier.padding(it),
                    directive = scaffoldNavigator.scaffoldDirective,
                    scaffoldState = scaffoldNavigator.scaffoldState,
                    listPane = {
                        AnimatedPane(
                            modifier = Modifier.preferredWidth(500.dp),
                        ) {
                            LazyColumn {
                                item {
                                    ElevatedCard(
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 6.dp
                                        ),
                                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateTo(
                                                    ListDetailPaneScaffoldRole.Detail, "1"
                                                )
                                            }
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(6.dp).fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.padding(6.dp).align(Start)
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
                                                            text = "Anonymous", fontSize = 20.sp
                                                        )
                                                        Text(
                                                            text = "Привет, давно тебя не было в уличных гонках!",
                                                            fontSize = 11.sp,
                                                            overflow = TextOverflow.Ellipsis,
                                                            maxLines = 1
                                                        )
                                                    }
                                                }
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.padding(6.dp).align(End)
                                                ) {
                                                    Badge(
                                                        modifier = Modifier.align(CenterVertically)
                                                            .padding(6.dp),
                                                        containerColor = Color.Red,
                                                        contentColor = Color.White,

                                                        ) {
                                                        Text("1")
                                                    }

                                                    var expanded by remember { mutableStateOf(false) }
                                                    IconButton(
                                                        modifier = Modifier.align(CenterVertically)
                                                            .padding(6.dp),
                                                        onClick = { expanded = !expanded }) {
                                                        Icon(
                                                            Icons.Default.MoreVert,
                                                            contentDescription = "More options"
                                                        )
                                                    }
                                                    DropdownMenu(
                                                        modifier = Modifier.align(CenterVertically),
                                                        expanded = expanded,
                                                        onDismissRequest = { expanded = false }) {
                                                        DropdownMenuItem(
                                                            onClick = { },
                                                            text = { Text("Скопировать") })
                                                        DropdownMenuItem(
                                                            onClick = { },
                                                            text = { Text("Вставить") })
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
                    },
                    detailPane = {
                        AnimatedPane {

                        }
                    })
            }
        }

    }
}
