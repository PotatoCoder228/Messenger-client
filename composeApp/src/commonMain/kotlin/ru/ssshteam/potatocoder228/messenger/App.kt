package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

var datastore = DataStore()

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PageRoutes.SignInPage.route) {

        composable(PageRoutes.MessagesPage.route) {
            AppTheme {
                MessagesPage(navController)

            }
        }


        composable(PageRoutes.SignInPage.route) {
            AppTheme {
                SignInPage(navController)
            }
        }

        composable(PageRoutes.RegistrationPage.route) {
            AppTheme {
                RegistrationPage(navController)
            }
        }
    }
}