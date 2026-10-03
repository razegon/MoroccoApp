package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.data.cache.ArcadeResultDao
import com.rezegon.moroccoapp.data.cache.toDomain
import com.rezegon.moroccoapp.data.cache.toEntity
import com.rezegon.moroccoapp.domain.model.ArcadeResult
import com.rezegon.moroccoapp.domain.repository.ArcadeResultRepository

class ArcadeResultRepositoryImpl(
    private val dao: ArcadeResultDao
) : ArcadeResultRepository {

    // Saves a completed Arcade game result in the local Room database.
    override suspend fun saveResult(result: ArcadeResult) {
        dao.insert(result.toEntity())
    }

    // Loads the best Arcade results from Room.
    override suspend fun getTopResults(limit: Int): List<ArcadeResult> {
        return dao.getTopResults(limit)
            .map { it.toDomain() }
    }
}