package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthDTO(val login: String, val password: String)