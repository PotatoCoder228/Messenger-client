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
data class NotificationDTO(
    val id: String = "",
    val chatId: String = "",
    val data: String = "",
    val sendAt: LocalDateTime? = null,
    val type: String = ""
)
