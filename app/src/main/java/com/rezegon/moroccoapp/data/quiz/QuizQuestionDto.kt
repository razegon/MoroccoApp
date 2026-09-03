package com.rezegon.moroccoapp.data.quiz

// Data Transfer Object matching the structure of questions.json.
data class QuizQuestionDto(
    val id: Int,
    val difficulty: String,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)