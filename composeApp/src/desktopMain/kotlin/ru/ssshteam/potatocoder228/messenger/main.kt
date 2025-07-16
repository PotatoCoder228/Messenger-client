package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.input.key.Key.Companion.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.cancel
import java.io.File
import java.util.concurrent.CancellationException

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
                } catch (e: Exception) {
                    e.printStackTrace()
                    val file = File("Cookie.db")
                    if(file.isFile){
                        file.delete()
                    }
                    datastore = DataStore()
                    fileChooser = FileChooser(window)
                }
            }
            App()
        }
    }
}