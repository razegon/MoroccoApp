package com.razegon.moroccoapp.data.repository

import com.razegon.shared.data.cache.ArcadeResultDao
import com.razegon.shared.data.cache.toDomain
import com.razegon.shared.data.cache.toEntity
import com.razegon.shared.domain.model.ArcadeResult
import com.razegon.shared.domain.repository.ArcadeResultRepository

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