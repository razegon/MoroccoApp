package com.rezegon.moroccoapp.data.cache

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface WikidataDao {

    @Query("SELECT * FROM WikidataEntity WHERE wikidataId = :wikidataId")
    suspend fun get(wikidataId: String): WikidataEntity?

    @Upsert
    suspend fun upsert(entity: WikidataEntity)
}