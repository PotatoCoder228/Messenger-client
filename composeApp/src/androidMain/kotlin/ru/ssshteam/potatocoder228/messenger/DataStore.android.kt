package ru.ssshteam.potatocoder228.messenger

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


actual class DataStore: AutoCloseable {

    private var databaseConnection: SQLiteDatabase?
    actual constructor(){
        databaseConnection = null
    }
    constructor(db : SQLiteDatabase){
        databaseConnection = db;
        databaseConnection?.execSQL(
             "CREATE TABLE IF NOT EXISTS Cookie (id INTEGER PRIMARY KEY, key TEXT UNIQUE, data TEXT)"
        )
    }

    actual fun saveCookie(key: String, data: String, daysToLive: Int) {
        databaseConnection?.compileStatement("INSERT INTO Cookie (key, DATA) VALUES (?,?) ON CONFLICT (key) DO UPDATE SET DATA = ?").use{
            stmt ->
            stmt?.bindString(1, key)
            stmt?.bindString(2, data)
            stmt?.bindString(3, data)
            stmt?.executeInsert()
        }
    }

    actual fun getCookie(key: String): String {
        var result = ""
        val query: Cursor? = databaseConnection?.rawQuery("SELECT * FROM users;", null)
        if (query?.moveToFirst() == true) {
            result = query.getString(0)
        }
        return result
    }

    actual override fun close() {
        databaseConnection?.close()
    }
}