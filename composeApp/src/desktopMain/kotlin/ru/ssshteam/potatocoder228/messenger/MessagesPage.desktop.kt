package ru.ssshteam.potatocoder228.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ssshteam.potatocoder228.messenger.dto.Emoji

sealed class EmojiPickerRoutes(val route: String) {

    data object FacesPage : EmojiPickerRoutes("Faces")
    data object ReactionsPage : EmojiPickerRoutes("Reactions")
    data object AnimalsPage : EmojiPickerRoutes("Animals")
    data object FoodPage : EmojiPickerRoutes("Food")
    data object PlacesPage : EmojiPickerRoutes("Places")
    data object ActivitiesPage : EmojiPickerRoutes("Activities")
    data object ObjectsPage : EmojiPickerRoutes("Objects")
    data object FlagsPage : EmojiPickerRoutes("Flags")
}

@Composable
fun EmojiGrid(
    group: String, input: String, onEmojiSelect: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth().requiredHeight(300.dp)
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Adaptive(minSize = 50.dp)
    ) {
        if (input == "") {
            val filter = { emoji: Emoji -> emoji.version < 13 && emoji.group == group }
            items(items = emojis!!.emojis.filter { emoji -> filter(emoji) }) { emoji ->
                TextButton(
                    modifier = Modifier.size(50.dp), onClick = { onEmojiSelect(emoji.emoji) }) {
                    Text(emoji.emoji, style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            val searchFilter = { keys: Set<String> ->
                keys.firstOrNull { key -> input in key } != null
            }
            val filter = { emoji: Emoji ->
                emoji.version < 13 && searchFilter(
                    emoji.keywords
                )
            }
            items(items = emojis!!.emojis.filter { emoji -> filter(emoji) }) { emoji ->
                TextButton(onClick = { onEmojiSelect(emoji.emoji) }) {
                    Text(emoji.emoji, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
actual fun EmojiPicker(
    modifier: Modifier, onEmojiSelect: (String) -> Unit
) {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }
    val indexGen = generateSequence(0) { it + 1 }.iterator()
    val tabs = mapOf(
        EmojiPickerRoutes.FacesPage.route to Pair("\uD83D\uDE00", indexGen.next()),
        EmojiPickerRoutes.ReactionsPage.route to Pair("\uD83D\uDC4B", indexGen.next()),
        EmojiPickerRoutes.AnimalsPage.route to Pair("\uD83D\uDC31", indexGen.next()),
        EmojiPickerRoutes.FoodPage.route to Pair("\uD83C\uDF4E", indexGen.next()),
        EmojiPickerRoutes.PlacesPage.route to Pair("\uD83C\uDFE0", indexGen.next()),
        EmojiPickerRoutes.ActivitiesPage.route to Pair("⚽", indexGen.next()),
        EmojiPickerRoutes.ObjectsPage.route to Pair("⛔", indexGen.next()),
        EmojiPickerRoutes.FlagsPage.route to Pair("\uD83C\uDFC1", indexGen.next()),
    )
    val emojiInput = remember {
        mutableStateOf("")
    }
    Column(modifier = modifier.width(250.dp)) {
        Row {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = emojiInput.value,
                onValueChange = { emojiInput.value = it },
                singleLine = true,
                placeholder = { Text("Search") },
                trailingIcon = {
                    val image = Icons.Filled.Clear

                    val description = "Очистить"

                    IconButton(onClick = { emojiInput.value = "" }) {
                        Icon(imageVector = image, description)
                    }
                })
        }
        LazyRow(modifier.requiredHeight(50.dp)) {
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedDestination,
                    modifier = Modifier.requiredHeight(50.dp).width(250.dp)
                        .align(CenterHorizontally),
                ) {
                    tabs.keys.forEach { item ->
                        Tab(
                            modifier = Modifier.align(CenterHorizontally).requiredHeight(50.dp),
                            selected = selectedDestination == (tabs[item]!!.second),
                            onClick = {
                                navController.navigate(route = item)
                                selectedDestination = tabs[item]!!.second
                            },
                            icon = {
                                Text(
                                    modifier = Modifier.align(CenterHorizontally),
                                    text = tabs[item]!!.first,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            })

                    }
                }
            }
        }
        NavHost(
            modifier = Modifier.align(CenterHorizontally),
            navController = navController,
            startDestination = EmojiPickerRoutes.FacesPage.route
        ) {
            composable(EmojiPickerRoutes.FacesPage.route) {
                EmojiGrid("smileys-emotion", emojiInput.value, onEmojiSelect)
            }

            composable(EmojiPickerRoutes.ReactionsPage.route) {
                EmojiGrid("people-body", emojiInput.value, onEmojiSelect)
            }

            composable(EmojiPickerRoutes.AnimalsPage.route) {
                EmojiGrid("animals-nature", emojiInput.value, onEmojiSelect)
            }
            composable(EmojiPickerRoutes.FoodPage.route) {
                EmojiGrid("food-drink", emojiInput.value, onEmojiSelect)
            }
            composable(EmojiPickerRoutes.PlacesPage.route) {
                EmojiGrid("travel-places", emojiInput.value, onEmojiSelect)
            }
            composable(EmojiPickerRoutes.ActivitiesPage.route) {
                EmojiGrid("activities", emojiInput.value, onEmojiSelect)
            }
            composable(EmojiPickerRoutes.ObjectsPage.route) {
                EmojiGrid("objects", emojiInput.value, onEmojiSelect)
            }

            composable(EmojiPickerRoutes.FlagsPage.route) {
                EmojiGrid("flags", emojiInput.value, onEmojiSelect)
            }

        }
    }
}