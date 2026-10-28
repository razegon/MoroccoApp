
package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikimediaImage

interface WikimediaPersistentCache {

    suspend fun get(pageId: Int): WikimediaImage?

    suspend fun put(pageId: Int, image: WikimediaImage)
}


