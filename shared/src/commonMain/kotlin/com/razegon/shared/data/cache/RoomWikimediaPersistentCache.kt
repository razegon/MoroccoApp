package com.razegon.shared.data.cache

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaPersistentCache

class RoomWikimediaPersistentCache(
    private val dao: WikimediaDao
) : WikimediaPersistentCache {

    override suspend fun get(pageId: Int): WikimediaImage? {
        return dao.get(pageId)?.toDomain()
    }

    override suspend fun put(pageId: Int, image: WikimediaImage) {
        dao.upsert(image.toEntity(pageId))
    }
}