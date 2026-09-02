package com.rezegon.moroccoapp.data.cache

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [WikidataEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wikidataDao(): WikidataDao
}