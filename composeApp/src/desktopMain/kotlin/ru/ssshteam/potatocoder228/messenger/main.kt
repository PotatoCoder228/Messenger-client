package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    configureSwingGlobalsForCompose()
    application {
        Window(
            onCloseRequest = { datastore?.close(); exitApplication() },
            title = "Messenger",
        ) {
            remember {
                try {
                    datastore = DataStore()
                    recorder = DesktopAudioRecorder()
                    fileChooser = DesktopFileChooser(window)
                } catch (e: Exception) {
                    e.printStackTrace()
                    val file = File("Cookie.db")
                    if (file.isFile) {
                        file.delete()
                    }
                    datastore = DataStore()
                    recorder = DesktopAudioRecorder()
                    fileChooser = DesktopFileChooser(window)
                }
            }
            App()
        }
    }
}