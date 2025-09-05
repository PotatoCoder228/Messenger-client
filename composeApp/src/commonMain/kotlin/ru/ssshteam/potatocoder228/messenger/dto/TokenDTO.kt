package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class TokenDTO @OptIn(ExperimentalUuidApi::class) constructor(
    var token: String,
    var refreshToken: String,
    var userId: Uuid
)