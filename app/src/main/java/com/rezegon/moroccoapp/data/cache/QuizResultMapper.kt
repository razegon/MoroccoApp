package com.rezegon.moroccoapp.data.cache

import com.rezegon.moroccoapp.domain.model.QuizResult

// Converts the Room entity into the domain model.
fun QuizResultEntity.toDomain(): QuizResult {
    return QuizResult(
        nickname = nickname,
        difficulty = difficulty,
        score = score,
        totalQuestions = totalQuestions,
        date = date
    )
}

// Converts the domain model into the Room entity.
// The database generates the entity ID automatically.
fun QuizResult.toEntity(): QuizResultEntity {
    return QuizResultEntity(
        nickname = nickname,
        difficulty = difficulty,
        score = score,
        totalQuestions = totalQuestions,
        date = date
    )
}