package com.rezegon.moroccoapp.domain.model

// Domain model used by the quiz logic and UI.
data class QuizQuestion(
    val difficulty: QuizDifficulty,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)