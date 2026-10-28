package com.razegon.shared.data.cache

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaCache

class WikimediaMemoryCache : WikimediaCache {

    private val cache = mutableMapOf<Int, WikimediaImage>()

    override fun get(pageId: Int): WikimediaImage? {
        return cache[pageId]
    }

    override fun put(
        pageId: Int,
        image: WikimediaImage
        ) {
        cache[pageId] = image
    }

    override fun clear() {
        cache.clear()
    }
}