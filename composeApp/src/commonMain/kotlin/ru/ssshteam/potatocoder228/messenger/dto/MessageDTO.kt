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
data class MessageDTO @OptIn(ExperimentalUuidApi::class) constructor(
    var id: String = "",
    var message: String = "",
    var senderId: String = "",
    var sender: String = "",
    var sendAt: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    var edited: LocalDateTime? = null,
    var repliedToId: Uuid = Uuid.NIL,
    var threadParentMsgId: Uuid = Uuid.NIL,
    var messageType: String = ""
)