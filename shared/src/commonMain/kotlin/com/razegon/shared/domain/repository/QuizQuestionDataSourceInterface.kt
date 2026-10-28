package com.razegon.shared.domain.repository

import com.razegon.shared.data.quiz.QuizQuestionDto

interface QuizQuestionDataSourceInterface {
    suspend fun loadQuestions(): List<QuizQuestionDto>
}