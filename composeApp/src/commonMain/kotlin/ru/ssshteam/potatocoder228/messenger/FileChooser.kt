package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.ssshteam.potatocoder228.messenger.internal.File

expect class FileChooser() {
    fun selectFile():
            SnapshotStateList<File>
}