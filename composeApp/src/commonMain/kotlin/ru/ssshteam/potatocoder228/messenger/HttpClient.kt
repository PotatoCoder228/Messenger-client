package ru.ssshteam.potatocoder228.messenger

import androidx.compose.runtime.MutableState
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import ru.ssshteam.potatocoder228.messenger.dto.TokenDTO

const val httpHost = "http://46.229.212.56:9087"
const val wsHost = "ws://46.229.212.56:9087/ws"
//const val httpHost = "http://localhost:9087"
//const val wsHost = "ws://localhost:9087/ws"

var token: MutableState<TokenDTO>? = null

val httpClient = HttpClient(CIO) {
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 5)
        exponentialDelay()
    }
    install(ContentNegotiation) {
        json()
    }
}
val wsClient = HttpClient() {
    install(WebSockets) {
        pingIntervalMillis = 20_000
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
val client =
    StompClient(KtorWebSocketClient(wsClient))