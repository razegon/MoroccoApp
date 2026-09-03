package com.rezegon.moroccoapp.data.quiz

import com.rezegon.moroccoapp.domain.model.QuizDifficulty
import com.rezegon.moroccoapp.domain.model.QuizQuestion

// Converts the external JSON model into the domain model used by the app.
fun QuizQuestionDto.toDomain(): QuizQuestion {
    return QuizQuestion(
        // JSON stores difficulty as text; the domain model uses a type-safe enum.
        difficulty = QuizDifficulty.valueOf(difficulty),
        question = question,
        answers = answers,
        correctAnswerIndex = correctAnswerIndex
    )
}