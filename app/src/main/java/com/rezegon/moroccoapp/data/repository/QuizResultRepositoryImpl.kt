package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.data.cache.QuizResultDao
import com.rezegon.moroccoapp.data.cache.toDomain
import com.rezegon.moroccoapp.data.cache.toEntity
import com.rezegon.moroccoapp.domain.model.QuizResult
import com.rezegon.moroccoapp.domain.repository.QuizResultRepository

class QuizResultRepositoryImpl(
    private val dao: QuizResultDao
) : QuizResultRepository {

    // Saves a completed quiz result in the local Room database.
    override suspend fun saveResult(result: QuizResult) {
        dao.insert(result.toEntity())
    }

    // Loads the best quiz results from Room.
    override suspend fun getTopResults(limit: Int): List<QuizResult> {
        return dao.getTopResults(limit)
            .map { it.toDomain() }
    }
}