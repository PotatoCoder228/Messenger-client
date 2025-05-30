package ru.ssshteam.potatocoder228.messenger

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Messenger",
    ) {
        datastore = DataStore()
        App()
    }
}