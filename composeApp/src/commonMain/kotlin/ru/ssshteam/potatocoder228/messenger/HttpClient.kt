package ru.ssshteam.potatocoder228.messenger

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import ru.ssshteam.potatocoder228.messenger.dto.TokenDTO

const val httpHost = "http://localhost:9087"
const val wsHost = "ws://localhost:9087"

var token: TokenDTO = TokenDTO("", 0)

val httpClient = HttpClient(CIO) {
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 5)
        exponentialDelay()
    }
    install(ContentNegotiation) {
        json()
    }
}
//val wsClient = HttpClient() {
//    install(WebSockets) {
//
//    }
//}
