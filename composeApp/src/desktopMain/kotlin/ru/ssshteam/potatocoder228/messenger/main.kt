package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.input.key.Key.Companion.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    configureSwingGlobalsForCompose()
    application {
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
}