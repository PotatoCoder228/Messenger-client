package ru.ssshteam.potatocoder228.messenger

expect class DataStore() : AutoCloseable {
    fun saveCookie(key: String, data: String, daysToLive: Int = 7)
    fun getCookie(key: String): String
    override fun close()
}