package com.rezegon.moroccoapp.domain.model

// Represents the final result of one completed Arcade game.
data class ArcadeResult(
    val nickname: String,
    val score: Int,
    val date: Long
)