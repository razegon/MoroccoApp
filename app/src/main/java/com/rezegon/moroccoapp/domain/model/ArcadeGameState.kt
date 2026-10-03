package com.rezegon.moroccoapp.domain.model

data class ArcadeGameState(
    val currentQuestion: QuizQuestion? = null,
    val selectedAnswerIndex: Int? = null,
    val isAnswerChecked: Boolean = false,
    val score: Int = 0,
    val remainingTimeSeconds: Int = 45,
    val usedQuestionIds: Set<Int> = emptySet(),
    val isGameOver: Boolean = false
)