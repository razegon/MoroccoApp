package com.rezegon.moroccoapp.data.cache

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        WikidataEntity::class,
        WikimediaEntity::class
               ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to persisted Wikidata information.
     */
    abstract fun wikidataDao(): WikidataDao

    /**
     * Provides access to persisted Wikimedia image metadata.
     */
    abstract fun wikimediaDao(): WikimediaDao
}