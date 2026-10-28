package com.razegon.moroccoapp.data.repository

import com.razegon.shared.data.cache.QuizResultDao
import com.razegon.shared.data.cache.toDomain
import com.razegon.shared.data.cache.toEntity
import com.razegon.shared.domain.model.QuizResult
import com.razegon.shared.domain.repository.QuizResultRepository

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