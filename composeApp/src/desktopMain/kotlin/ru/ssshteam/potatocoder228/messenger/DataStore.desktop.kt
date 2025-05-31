package ru.ssshteam.potatocoder228.messenger

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL

actual class DataStore actual constructor() : AutoCloseable {
    private var databaseConnection = BundledSQLiteDriver().open("Cookie.db")

    init {
        databaseConnection.prepare("PRAGMA journal_mode=DELETE").step();
        databaseConnection.prepare("PRAGMA cipher = 'chacha20'").step();
        databaseConnection.prepare("PRAGMA key = 'MyBigKey'").step();
        databaseConnection.execSQL(
            "CREATE TABLE IF NOT EXISTS Cookie (id INTEGER PRIMARY KEY, key TEXT UNIQUE, data TEXT)"
        )
    }

    actual fun saveCookie(key: String, data: String, daysToLive: Int) {
        databaseConnection.prepare(
            "INSERT INTO Cookie (key, DATA) VALUES (?,?) ON CONFLICT (key) DO UPDATE SET DATA = ?"
        ).use { stmt ->
            stmt.bindText(index = 1, value = key)
            stmt.bindText(index = 2, value = data)
            stmt.bindText(index = 3, value = data)
            stmt.step()
        }
    }

    actual fun getCookie(key: String): String {
        var result = ""
        databaseConnection.prepare("SELECT key FROM Cookie WHERE key = ?").use { stmt ->
            stmt.bindText(index = 1, value = key)
            while (stmt.step()) {
                result = stmt.getText(0)
            }
        }
        return result
    }

    actual override fun close() {
        databaseConnection.close()
    }
}