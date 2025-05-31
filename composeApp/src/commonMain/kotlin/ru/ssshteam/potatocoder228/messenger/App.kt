package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.ssshteam.potatocoder228.messenger.viewmodels.AppViewModel

var datastore: DataStore? = null

@Composable
@Preview
fun App(viewModel: AppViewModel = viewModel { AppViewModel() }) {
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