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
        ORDER BY 
            score DESC, 
            CASE difficulty
                WHEN 'VERY_HARD' THEN 5
                WHEN 'HARD' THEN 4
                WHEN 'MEDIUM' THEN 3
                WHEN 'EASY' THEN 2
                WHEN 'VERY_EASY' THEN 1
                ELSE 0
            END DESC,
            date ASC
        LIMIT :limit
        """
    )
    suspend fun getTopResults(limit: Int): List<QuizResultEntity>
}