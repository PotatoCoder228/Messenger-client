package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(val token: String, val refreshToken: String, val userId: String)