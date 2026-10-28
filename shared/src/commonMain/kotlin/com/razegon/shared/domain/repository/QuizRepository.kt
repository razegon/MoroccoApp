package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.domain.model.QuizQuestion

interface QuizRepository {

    suspend fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion>

    suspend fun getAllQuestions(): List<QuizQuestion>
}