
package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikimediaImage

interface WikimediaPersistentCache {

    suspend fun get(pageId: Int): WikimediaImage?

    suspend fun put(pageId: Int, image: WikimediaImage)
}


