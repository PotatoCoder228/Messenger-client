package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.ssshteam.potatocoder228.messenger.dto.EmojiDataDTO
import ru.ssshteam.potatocoder228.messenger.dto.TokenDTO
import ru.ssshteam.potatocoder228.messenger.viewmodels.AppViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

var datastore: DataStore? = null
var fileChooser: FileChooser? = null
var emojis: EmojiDataDTO? = null
var username: String = ""

@OptIn(ExperimentalUuidApi::class)
@Composable
fun App(viewModel: AppViewModel = viewModel { AppViewModel() }) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var defaultRoute = PageRoutes.SignInPage.route
    remember {
        scope.launch {
            emojis = Json.decodeFromString<EmojiDataDTO>(emojiJson)
            emojiJson = ""
        }
        if (datastore?.getCookie("savePass").equals("true")) {
            token = mutableStateOf(
                TokenDTO(
                    datastore?.getCookie("token")!!,
                    datastore?.getCookie("refreshToken")!!,
                    Uuid.parse(datastore?.getCookie("userId")!!)
                )
            )
            if (token?.value?.token != null) {
                defaultRoute = PageRoutes.MessagesPage.route
            }
        }
    }

    NavHost(navController = navController, startDestination = defaultRoute) {

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