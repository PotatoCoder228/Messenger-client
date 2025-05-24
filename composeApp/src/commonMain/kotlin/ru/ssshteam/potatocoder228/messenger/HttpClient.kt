package ru.ssshteam.potatocoder228.messenger

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import ru.ssshteam.potatocoder228.messenger.dto.TokenDTO

const val httpHost = "http://localhost:9087"
const val wsHost = "ws://localhost:9087"

var token: TokenDTO = TokenDTO("", 0)

val httpClient = HttpClient() {
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

    }
}

//client.get("https://ktor.io") {
//    headers {
//        append(HttpHeaders.Accept, "text/html")
//        append(HttpHeaders.Authorization, "abc123")
//        append(HttpHeaders.UserAgent, "ktor client")
//    }
//}