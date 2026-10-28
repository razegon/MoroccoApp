package com.rezegon.shared.domain.repository

import com.rezegon.shared.data.quiz.QuizQuestionDto

interface QuizQuestionDataSourceInterface {
    suspend fun loadQuestions(): List<QuizQuestionDto>
}