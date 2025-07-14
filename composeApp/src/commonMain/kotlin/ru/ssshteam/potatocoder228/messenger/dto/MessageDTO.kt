package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class MessageDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = "",
    val message: String = "",
    val senderId: String = "",
    val sender: String = "",
    val sendAt: String = "",
    val edited: String = "",
    val repliedToId: Uuid = Uuid.NIL,
    val threadParentMsgId: Uuid = Uuid.NIL,
    val messageType: String = ""
)