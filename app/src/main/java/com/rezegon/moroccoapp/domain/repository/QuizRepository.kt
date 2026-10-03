package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.QuizDifficulty
import com.rezegon.moroccoapp.domain.model.QuizQuestion

interface QuizRepository {

    fun getQuestions(
        difficulty: QuizDifficulty
    ): List<QuizQuestion>

    fun getAllQuestions(): List<QuizQuestion>
}