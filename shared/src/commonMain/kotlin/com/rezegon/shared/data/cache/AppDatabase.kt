package com.rezegon.shared.data.cache

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(
    entities = [
        WikidataEntity::class,
        WikimediaEntity::class,
        QuizResultEntity::class,
        ArcadeResultEntity::class
               ],
    version = 2
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to persisted Wikidata information.
     */
    abstract fun wikidataDao(): WikidataDao

    /**
     * Provides access to persisted Wikimedia image metadata.
     */
    abstract fun wikimediaDao(): WikimediaDao

    /**
     * Provides access to persisted Quiz results.
     */
    abstract fun quizResultDao(): QuizResultDao

    /**
     * Provides access to persisted Arcade results.
     */
    abstract fun arcadeResultDao(): ArcadeResultDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor :
    RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}