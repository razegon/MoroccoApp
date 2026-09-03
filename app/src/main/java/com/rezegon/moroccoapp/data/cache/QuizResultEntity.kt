package com.rezegon.moroccoapp.data.cache

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Stores one completed quiz result in the local Room database.
@Entity
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Long
)



