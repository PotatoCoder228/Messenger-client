package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@Immutable
data class UserInChatDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val login: String,
    val role: String,
    val id: Uuid,
    val status: String,
    val lastOnlineTime: LocalDateTime
)
