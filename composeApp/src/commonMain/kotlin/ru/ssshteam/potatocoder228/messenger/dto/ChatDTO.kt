package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ChatDTO(val id: Int, val name: String, val role: String) {
    constructor(id: Int) : this(id, "", "")
}