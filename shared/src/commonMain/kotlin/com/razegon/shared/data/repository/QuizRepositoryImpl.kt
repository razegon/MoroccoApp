package com.razegon.shared.data.repository

import com.razegon.shared.data.mapper.toDomain
import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.domain.model.QuizQuestion
import com.razegon.shared.domain.repository.QuizQuestionDataSourceInterface
import com.razegon.shared.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val dataSource: QuizQuestionDataSourceInterface
) : QuizRepository {

    // Loads questions from the JSON data source and converts them to domain models.
    override suspend fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion> {
        return dataSource
            .loadQuestions()
            .map { it.toDomain() }
            .filter { it.difficulty == difficulty }
    }

    // Loads all questions to Arcade Quiz Mode
    override suspend fun getAllQuestions(): List<QuizQuestion> {
        return dataSource
            .loadQuestions()
            .map { it.toDomain() }
    }
}