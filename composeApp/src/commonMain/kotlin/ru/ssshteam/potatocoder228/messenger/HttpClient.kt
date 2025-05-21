package ru.ssshteam.potatocoder228.messenger

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets

val httpClient = HttpClient()
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