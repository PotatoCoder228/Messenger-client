package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.ssshteam.potatocoder228.messenger.internal.File

interface FileChooser {
    suspend fun lockChooser()
    fun addFileInputStream(path: String, stream: Any)
    fun addFile(file: File)
    fun getFileInputStream(path: String): Any
    fun addPathForDownloading(path: String)
    fun addFileOutputStream(path: String, stream: Any)
    fun removeFileOutputStream(path: String)
    fun removeFileInputStream(path: String)
    fun getFileOutputStream(path: String): Any
    fun unlockChooser()
    suspend fun selectFile():
            SnapshotStateList<File>

    suspend fun selectDownloadingFilepath(filename: String = ""): String
}