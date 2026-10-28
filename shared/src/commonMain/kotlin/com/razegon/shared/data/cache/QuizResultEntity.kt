package com.razegon.shared.data.cache

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.razegon.shared.domain.model.QuizDifficulty

// Stores one completed quiz result in the local Room database.
@Entity
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val difficulty: QuizDifficulty,
    val score: Int,
    val totalQuestions: Int,
    val date: Long
)



