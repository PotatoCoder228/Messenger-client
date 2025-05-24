package ru.ssshteam.potatocoder228.messenger.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(val id: Int, val name: String, val role: String) {
    constructor(id: Int) : this(id, "", "")
}