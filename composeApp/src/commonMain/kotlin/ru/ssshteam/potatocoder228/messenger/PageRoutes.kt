package ru.ssshteam.potatocoder228.messenger

sealed class PageRoutes(val route: String) {

    data object MessagesPage : PageRoutes("MessagesPage")
    data object SignInPage : PageRoutes("SignInPage")
    data object RegistrationPage : PageRoutes("RegistrationPage")
}