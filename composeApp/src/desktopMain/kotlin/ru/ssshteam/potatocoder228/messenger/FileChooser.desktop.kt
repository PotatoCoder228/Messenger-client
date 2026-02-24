package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.awt.ComposeWindow
import ru.ssshteam.potatocoder228.messenger.internal.File
import java.awt.Dimension
import java.awt.FileDialog
import java.awt.FileDialog.SAVE
import javax.swing.UIManager

class DesktopFileChooser(window: ComposeWindow) : FileChooser {
    private var window: ComposeWindow? = window

    init {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }

    override suspend fun lockChooser() {

    }

    override fun addFileInputStream(path: String, stream: Any) {

    }

    override fun addFile(file: File) {

    }

    override fun getFileInputStream(path: String): Any {
        return "" as Any
    }

    override fun addPathForDownloading(path: String) {
    }

    override fun addFileOutputStream(path: String, stream: Any) {
    }

    override fun removeFileOutputStream(path: String) {
    }

    override fun removeFileInputStream(path: String) {
    }

    override fun getFileOutputStream(path: String): Any {
        return ""
    }

    override fun unlockChooser() {

    }

    override suspend fun selectFile():
            SnapshotStateList<File> {
        val fileDialog = FileDialog(window)
        fileDialog.size = Dimension(500, 500)
        fileDialog.isVisible = true
        fileDialog.isMultipleMode = true
        val filesList:
                SnapshotStateList<File> = mutableStateListOf()
        fileDialog.files.forEach { file ->
            filesList.add(
                File(
                    file.absolutePath,
                    file.name,
                    file.extension,
                    file.totalSpace,
                    "file://${file.path}"
                )
            )
        }
        return filesList
    }

    override suspend fun selectDownloadingFilepath(filename: String): String {
        val fileDialog = FileDialog(window)
        fileDialog.mode = SAVE
        fileDialog.file = filename
        fileDialog.size = Dimension(500, 500)
        fileDialog.isVisible = true
        fileDialog.isMultipleMode = false
        return fileDialog.directory + fileDialog.file
    }
}