package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

var datastore: DataStore? = null

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    var theme by rememberSaveable { mutableStateOf(false) }
    val onThemeChange = { theme = !theme }
    NavHost(navController = navController, startDestination = PageRoutes.SignInPage.route) {

        composable(PageRoutes.MessagesPage.route) {
            AppTheme(theme) {
                MessagesPage(navController, onThemeChange)

            }
        }


        composable(PageRoutes.SignInPage.route) {
            AppTheme(theme) {
                SignInPage(navController, onThemeChange)
            }
        }

        composable(PageRoutes.RegistrationPage.route) {
            AppTheme(theme) {
                RegistrationPage(navController, onThemeChange)
            }
        }
    }
}