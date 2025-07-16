package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class ChatDTO(
    val id: String,
    val name: String = "",
    val role: String = "",
    var lastEnter: LocalDateTime? = null
)