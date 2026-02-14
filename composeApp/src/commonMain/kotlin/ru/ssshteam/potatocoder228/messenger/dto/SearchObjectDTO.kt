package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Immutable
@JsonIgnoreUnknownKeys
data class SearchObjectDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val type: String = "",
    val name: String = "",
    val description: String = "",
    val id: Uuid = Uuid.NIL,
    val logoId: Uuid = Uuid.NIL,
)