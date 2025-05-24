package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable


@Serializable
data class MessageDTO(
    val id: Int,
    val message: String,
    val senderId: Int,
    val sender: String,
    val sendTime: String
)