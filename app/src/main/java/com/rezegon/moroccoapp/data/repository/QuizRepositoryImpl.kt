package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.data.quiz.QuizQuestionDataSource
import com.rezegon.moroccoapp.data.quiz.toDomain
import com.rezegon.moroccoapp.domain.model.QuizDifficulty
import com.rezegon.moroccoapp.domain.model.QuizQuestion
import com.rezegon.moroccoapp.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val dataSource: QuizQuestionDataSource
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
}

