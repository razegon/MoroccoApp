package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.ArcadeResult

interface ArcadeResultRepository {

    // Stores a completed Arcade game result.
    suspend fun saveResult(result: ArcadeResult)

    // Returns the best Arcade results up to the requested limit.
    suspend fun getTopResults(limit: Int): List<ArcadeResult>
}