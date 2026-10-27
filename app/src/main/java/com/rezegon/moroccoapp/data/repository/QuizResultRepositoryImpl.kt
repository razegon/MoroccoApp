package com.rezegon.moroccoapp.data.repository

import com.rezegon.shared.data.cache.QuizResultDao
import com.rezegon.shared.data.cache.toDomain
import com.rezegon.shared.data.cache.toEntity
import com.rezegon.shared.domain.model.QuizResult
import com.rezegon.shared.domain.repository.QuizResultRepository

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