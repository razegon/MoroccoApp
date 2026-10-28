package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.QuizDifficulty
import com.rezegon.shared.domain.model.QuizQuestion

interface QuizRepository {

    suspend fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion>

    suspend fun getAllQuestions(): List<QuizQuestion>
}