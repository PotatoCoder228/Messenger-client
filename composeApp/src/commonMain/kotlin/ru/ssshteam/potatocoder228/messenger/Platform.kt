package ru.ssshteam.potatocoder228.messenger

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform