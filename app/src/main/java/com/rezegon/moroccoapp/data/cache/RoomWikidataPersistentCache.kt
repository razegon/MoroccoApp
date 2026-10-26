
package com.rezegon.moroccoapp.data.cache

import com.rezegon.shared.domain.model.WikidataInfo
import com.rezegon.shared.domain.repository.WikidataPersistentCache

class RoomWikidataPersistentCache(
    private val dao: WikidataDao
) : WikidataPersistentCache {

    override suspend fun get(wikidataId: String): WikidataInfo? {
        return dao.get(wikidataId)?.toDomain()
    }

    override suspend fun put(info: WikidataInfo) {
        dao.upsert(info.toEntity())
    }
}


