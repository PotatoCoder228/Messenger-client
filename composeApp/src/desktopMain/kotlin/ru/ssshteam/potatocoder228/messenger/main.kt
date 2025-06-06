package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Messenger",
    ) {
        remember {
            try {
                datastore = DataStore()
                fileChooser = FileChooser(window)
            } catch (_: Exception) {

            }
        }
        App()
    }
}