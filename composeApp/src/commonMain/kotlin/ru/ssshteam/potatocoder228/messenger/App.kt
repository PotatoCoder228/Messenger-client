package ru.ssshteam.potatocoder228.messenger

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
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