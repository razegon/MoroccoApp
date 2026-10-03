package com.rezegon.moroccoapp.data.cache

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface ArcadeResultDao {

    // Stores a completed Arcade game result in Room.
    @Insert
    suspend fun insert(result: ArcadeResultEntity)

    // Returns the best Arcade results sorted by score and then by date.
    @Query(
        """
        SELECT * FROM ArcadeResultEntity
        ORDER BY score DESC, date ASC
        LIMIT :limit
        """
    )
    suspend fun getTopResults(limit: Int): List<ArcadeResultEntity>
}

