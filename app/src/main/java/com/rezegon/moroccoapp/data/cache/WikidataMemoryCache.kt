package com.rezegon.moroccoapp.data.cache

import com.rezegon.shared.domain.model.WikidataInfo
import com.rezegon.shared.domain.repository.WikidataCache

class WikidataMemoryCache : WikidataCache {

    private val cache = mutableMapOf<String, WikidataInfo>()

    override fun get(wikidataId: String): WikidataInfo? {
        return cache[wikidataId]
    }

    override fun put(info: WikidataInfo) {
        info.wikidataId?.let { id ->
            cache[id] = info
        }
    }

    override fun clear() {
        cache.clear()
    }
}



