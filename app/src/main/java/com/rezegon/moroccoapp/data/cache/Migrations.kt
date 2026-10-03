package com.rezegon.moroccoapp.data.cache

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

// Adds the Arcade results table while preserving existing database data.
val MIGRATION_1_2 = object : Migration(1, 2) {

    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS ArcadeResultEntity (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nickname TEXT NOT NULL,
                score INTEGER NOT NULL,
                date INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}
