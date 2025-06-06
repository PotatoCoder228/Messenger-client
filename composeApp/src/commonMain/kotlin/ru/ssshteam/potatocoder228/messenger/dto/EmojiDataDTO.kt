package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Emoji(
    val emoji: String,
    val version: Float,
    val group: String,
    val keywords: Set<String>
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class EmojiDataDTO(val emojis: List<Emoji>)