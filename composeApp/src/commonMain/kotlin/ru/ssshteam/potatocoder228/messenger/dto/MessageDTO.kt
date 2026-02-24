package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import ru.ssshteam.potatocoder228.messenger.dto.internal.FileView
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Stable
@JsonIgnoreUnknownKeys
data class MessageDTO @OptIn(ExperimentalUuidApi::class) constructor(
    var id: Uuid = Uuid.NIL,
    var message: String = "",
    var senderId: Uuid = Uuid.NIL,
    var sender: String = "",
    var sendAt: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
    var edited: LocalDateTime? = null,
    var repliedToId: Uuid = Uuid.NIL,
    var threadParentMsgId: Uuid = Uuid.NIL,
    var messageType: String = "",
    var newThreadMessages: Int = 0,
    var filesUrls: Array<FileView> = arrayOf(),
    @Transient
    var unreadMessages: Int = 0,
)