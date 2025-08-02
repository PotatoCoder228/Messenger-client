package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class NotificationDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.NIL,
    val chatId: Uuid = Uuid.NIL,
    val data: String = "",
    val sendAt: LocalDateTime? = null,
    val type: String = ""
)
