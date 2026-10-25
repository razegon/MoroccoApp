package com.rezegon.shared.data.repository

import com.rezegon.shared.data.mapper.toDomain
import com.rezegon.shared.domain.model.QuizDifficulty
import com.rezegon.shared.domain.model.QuizQuestion
import com.rezegon.shared.domain.repository.QuizQuestionDataSourceInterface
import com.rezegon.shared.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val dataSource: QuizQuestionDataSourceInterface
) : QuizRepository {

    // Loads questions from the JSON data source and converts them to domain models.
    override fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion> {
        return dataSource
            .loadQuestions()
            .map { it.toDomain() }
            .filter { it.difficulty == difficulty }
    }

    // Loads all questions to Arcade Quiz Mode
    override fun getAllQuestions(): List<QuizQuestion> {
        return dataSource
            .loadQuestions()
            .map { it.toDomain() }
    }
}