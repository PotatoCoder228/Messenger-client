package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class MessageDTO(
    val id: Int = 0,
    val message: String = "",
    val senderId: Int = 0,
    val sender: String = "",
    val sendTime: String = ""
)