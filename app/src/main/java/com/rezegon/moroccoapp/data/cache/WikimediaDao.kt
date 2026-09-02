package com.rezegon.moroccoapp.data.cache

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

/**
 * Provides access to persisted Wikimedia image metadata.
 */
@Dao
interface WikimediaDao {

    /**
     * Returns cached metadata for a Wikimedia page.
     *
     * The pageId is the primary key of WikimediaEntity.
     */
    @Query("SELECT * FROM WikimediaEntity WHERE pageId = :pageId")
    suspend fun get(pageId: Int): WikimediaEntity?

    /**
     * Inserts or updates Wikimedia image metadata.
     */
    @Upsert
    suspend fun upsert(entity: WikimediaEntity)
}

