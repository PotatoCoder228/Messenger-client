package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatCreateDTO(val name: String, val users: MutableList<String>)
