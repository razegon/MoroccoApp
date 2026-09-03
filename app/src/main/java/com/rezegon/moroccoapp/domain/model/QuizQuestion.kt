package com.rezegon.moroccoapp.domain.model

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)