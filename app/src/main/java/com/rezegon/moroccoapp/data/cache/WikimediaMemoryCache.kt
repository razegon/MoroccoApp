package com.rezegon.moroccoapp.data.cache

import com.rezegon.moroccoapp.domain.model.WikimediaImage
import com.rezegon.moroccoapp.domain.repository.WikimediaCache

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