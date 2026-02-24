package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@Stable
data class TokenDTO @OptIn(ExperimentalUuidApi::class) constructor(
    var token: String,
    var refreshToken: String,
    var userId: Uuid
)