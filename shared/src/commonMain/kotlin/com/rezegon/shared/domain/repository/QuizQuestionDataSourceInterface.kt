package com.rezegon.shared.domain.repository

import com.rezegon.shared.data.quiz.QuizQuestionDto

interface QuizQuestionDataSourceInterface {
    fun loadQuestions(): List<QuizQuestionDto>
}