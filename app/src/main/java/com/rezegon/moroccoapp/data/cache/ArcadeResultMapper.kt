package com.rezegon.moroccoapp.data.cache

import com.rezegon.moroccoapp.domain.model.ArcadeResult

// Converts the Room entity into a domain model so the rest of the app stays independent of Room.
fun ArcadeResultEntity.toDomain(): ArcadeResult {
    return ArcadeResult(
        nickname = nickname,
        score = score,
        date = date
    )
}

// Converts the domain result into a Room entity ready to be persisted.
fun ArcadeResult.toEntity(): ArcadeResultEntity {
    return ArcadeResultEntity(
        nickname = nickname,
        score = score,
        date = date
    )
}