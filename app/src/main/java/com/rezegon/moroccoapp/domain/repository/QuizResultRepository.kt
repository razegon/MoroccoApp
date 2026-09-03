package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.QuizResult

interface QuizResultRepository {

    // Stores a completed quiz result.
    suspend fun saveResult(result: QuizResult)

    // Returns the best quiz results up to the requested limit.
    suspend fun getTopResults(limit: Int): List<QuizResult>
}