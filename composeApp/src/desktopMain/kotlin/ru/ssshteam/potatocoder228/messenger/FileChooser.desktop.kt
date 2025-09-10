package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.awt.ComposeWindow
import ru.ssshteam.potatocoder228.messenger.internal.File
import java.awt.Dimension
import java.awt.FileDialog
import java.awt.FileDialog.LOAD
import java.awt.FileDialog.SAVE
import java.nio.file.Paths
import javax.swing.UIManager

actual class FileChooser {
    private var window: ComposeWindow? = null

    actual constructor() {
    }

    constructor(window: ComposeWindow) {
        this.window = window
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }

    actual fun selectFile():
            SnapshotStateList<File> {
        val fileDialog = FileDialog(window)
        fileDialog.size = Dimension(500, 500)
        fileDialog.isVisible = true
        fileDialog.isMultipleMode = true
        val filesList:
                SnapshotStateList<File> = mutableStateListOf()
        fileDialog.files.forEach { file -> filesList.add(File(file.absolutePath, file.name, file.extension)) }
        return filesList
    }

    actual fun selectDownloadingFilepath(filename: String): String{
        val fileDialog = FileDialog(window)
        fileDialog.mode = SAVE
        fileDialog.file = filename
        fileDialog.size = Dimension(500, 500)
        fileDialog.isVisible = true
        fileDialog.isMultipleMode = false
        return fileDialog.directory+ fileDialog.file
    }
}