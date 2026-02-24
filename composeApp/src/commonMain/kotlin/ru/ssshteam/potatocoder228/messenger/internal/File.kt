package ru.ssshteam.potatocoder228.messenger.internal

import androidx.compose.runtime.Immutable

@Immutable
data class File(
    val path: String,
    val filename: String,
    val extension: String,
    val size: Long = 0,
    val uri: String = ""
)
