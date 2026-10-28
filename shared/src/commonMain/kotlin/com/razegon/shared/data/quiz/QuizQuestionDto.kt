package com.razegon.shared.data.quiz

import kotlinx.serialization.Serializable

// Data Transfer Object matching the structure of questions.json.
@Serializable
data class QuizQuestionDto(
    val id: Int,
    val difficulty: String,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)