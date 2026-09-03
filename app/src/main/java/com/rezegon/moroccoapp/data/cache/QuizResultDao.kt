package com.rezegon.moroccoapp.data.cache

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface QuizResultDao {

    // Stores a completed quiz result in Room.
    @Insert
    suspend fun insert(result: QuizResultEntity)

    // Returns the best quiz results sorted by score and then by date.
    @Query(
        """
        SELECT * FROM QuizResultEntity
        ORDER BY score DESC, date ASC
        LIMIT :limit
        """
    )
    suspend fun getTopResults(limit: Int): List<QuizResultEntity>
}