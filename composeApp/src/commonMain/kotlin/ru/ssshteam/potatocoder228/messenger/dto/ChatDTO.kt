package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class ChatDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.NIL,
    val name: String = "",
    val role: String = "",
    var newMessages: Int? = null,
    @Transient
    var unreadedMessages: Int = 0,
)