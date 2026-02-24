package ru.ssshteam.potatocoder228.messenger.dto

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable


@Serializable
@Stable
class OperationDTO<T> {
    var data: T? = null

    var operation: String = ""

    companion object {
        var ADD: String = "ADD"

        var DELETE: String = "DELETE"

        var UPDATE: String = "UPDATE"
    }
}
