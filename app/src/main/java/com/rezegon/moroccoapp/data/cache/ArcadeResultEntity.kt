package com.rezegon.moroccoapp.data.cache

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Stores one completed Arcade game result in the local Room database.
@Entity
data class ArcadeResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val score: Int,
    val date: Long
)
