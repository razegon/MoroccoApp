package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.QuizDifficulty
import com.rezegon.shared.domain.model.QuizQuestion

interface QuizRepository {

    fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion>

    fun getAllQuestions(): List<QuizQuestion>
}