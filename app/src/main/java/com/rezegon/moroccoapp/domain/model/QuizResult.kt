package com.rezegon.moroccoapp.domain.model

// Represents one completed quiz result stored for the player ranking.
data class QuizResult(
    val nickname: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Long
)