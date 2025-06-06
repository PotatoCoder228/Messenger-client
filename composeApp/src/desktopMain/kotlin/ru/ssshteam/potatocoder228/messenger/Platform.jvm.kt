package ru.ssshteam.potatocoder228.messenger

class JVMPlatform : Platform {
    override val name: String = "Java"
}

actual fun getPlatform(): Platform = JVMPlatform()