package ru.ssshteam.potatocoder228.messenger

sealed class PageRoutes(val route: String) {

    data object MessagesPage : PageRoutes("MessagesPage")
    data object SettingsPage : PageRoutes("SettingsPage")
    data object SignInPage : PageRoutes("SignInPage")
    data object RegistrationPage : PageRoutes("RegistrationPage")
}


sealed class ProfilePopupRoutes(val route: String) {

    data object MainPage : ProfilePopupRoutes("MainPage")
    data object UsersPage : ProfilePopupRoutes("UsersPage")
    data object AdminsPage : ProfilePopupRoutes("AdminsPage")
    data object AccessSettings : ProfilePopupRoutes("AccessSettings")
}

sealed class ChatsSearchBarResultsRoutes(val route: String) {

    data object ChatsPage : ChatsSearchBarResultsRoutes("ChatsPage")
    data object UsersPage : ChatsSearchBarResultsRoutes("UsersPage")
}

enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    CHATS("chats", "Chats", "Chats"),
    USERS("users", "Users", "Users"),
}