package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.ssshteam.potatocoder228.messenger.dto.EmojiDataDTO
import ru.ssshteam.potatocoder228.messenger.viewmodels.AppViewModel

var datastore: DataStore? = null
var fileChooser: FileChooser? = null
var emojis: EmojiDataDTO? = null
var username: String = ""

@Composable
fun App(viewModel: AppViewModel = viewModel { AppViewModel() }) {
    val scope = rememberCoroutineScope()
    rememberSaveable {
        scope.launch {
            emojis = Json.decodeFromString<EmojiDataDTO>(emojiJson)
            emojiJson = ""
        }
    }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PageRoutes.SignInPage.route) {

        composable(PageRoutes.MessagesPage.route) {
            AppTheme(viewModel.theme.value) {
                MessagesPage(navController, viewModel.onThemeChange)
            }
        }


        composable(PageRoutes.SignInPage.route) {
            AppTheme(viewModel.theme.value) {
                SignInPage(navController, viewModel.onThemeChange)
            }
        }

        composable(PageRoutes.RegistrationPage.route) {
            AppTheme(viewModel.theme.value) {
                RegistrationPage(navController, viewModel.onThemeChange)
            }
        }
    }
}