package ru.ssshteam.potatocoder228.messenger

import kotlinx.browser.document

external fun encodeURIComponent(str: String?): String
external fun decodeURIComponent(str: String?): String
external class Date() : JsAny {
    constructor(str: String)

    fun setTime(milliseconds: Double)
    fun getTime(): Double
    fun toUTCString(): String
}

external object JSON : JsAny {
    fun stringify(o: String?): String
    fun parse(text: String): String
}

actual class DataStore : AutoCloseable {

    actual fun saveCookie(key: String, data: String, daysToLive: Int) {
        val dataString = JSON.stringify(data);
        val encodedData = encodeURIComponent(dataString);

        val date = Date();
        date.setTime(date.getTime() + (daysToLive * 24 * 60 * 60 * 1000));
        val expires = "expires=${date.toUTCString()}";

        document.cookie = "${key}=${encodedData}; ${expires}; path=/; SameSite=None; Secure;";
    }

    actual fun getCookie(key: String): String {
        var res = ""
        val cookies = document.cookie.split(";")
        val cookie = cookies.find({ c -> c.startsWith("${key}=") })

        if (cookie.isNullOrEmpty()) return res;

        val encodedData = cookie.split('=')[1];
        val dataString = decodeURIComponent(encodedData);
        res = JSON.parse(dataString)
        return res;
    }

    actual override fun close() {

    }
}