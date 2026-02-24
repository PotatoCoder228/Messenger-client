package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Stable
@JsonIgnoreUnknownKeys
data class SearchDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.NIL,
    val name: String = "",
    val description: String = "",
    val date: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0),
)