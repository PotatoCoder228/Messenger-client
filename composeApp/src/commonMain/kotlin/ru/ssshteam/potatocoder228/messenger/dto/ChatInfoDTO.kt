package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class ChatInfoDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.NIL,
    val name: String = "",
    val members: List<UserInChatDTO> = listOf<UserInChatDTO>(),
)