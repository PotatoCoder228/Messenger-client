package ru.ssshteam.potatocoder228.messenger

import androidx.compose.ui.awt.ComposeWindow
import java.awt.Dimension
import java.awt.FileDialog
import javax.swing.UIManager

actual class FileChooser {
    private var window: ComposeWindow? = null

    constructor(window: ComposeWindow) {
        this.window = window
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }

    actual fun selectFile(): String? {
        val fileDialog = FileDialog(window)
        fileDialog.size = Dimension(500, 500)
        fileDialog.isVisible = true
        return fileDialog.file
    }
}