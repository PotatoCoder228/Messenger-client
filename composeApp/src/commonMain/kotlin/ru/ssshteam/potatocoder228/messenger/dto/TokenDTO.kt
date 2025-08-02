package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class TokenDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val token: String,
    val refreshToken: String,
    val userId: Uuid
)