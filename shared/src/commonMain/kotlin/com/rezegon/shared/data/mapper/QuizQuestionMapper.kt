package com.rezegon.shared.data.mapper

import com.rezegon.shared.data.quiz.QuizQuestionDto
import com.rezegon.shared.domain.model.QuizDifficulty
import com.rezegon.shared.domain.model.QuizQuestion

// Converts the external JSON model into the domain model used by the app.
fun QuizQuestionDto.toDomain(): QuizQuestion {
    return QuizQuestion(
        // JSON stores difficulty as text; the domain model uses a type-safe enum.
        id = id,
        difficulty = QuizDifficulty.valueOf(difficulty),
        question = question,
        answers = answers,
        correctAnswerIndex = correctAnswerIndex
    )
}