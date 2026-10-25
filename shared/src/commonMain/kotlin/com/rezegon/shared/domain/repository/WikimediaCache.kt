package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikimediaImage

interface WikimediaCache {

    fun get(pageId: Int): WikimediaImage?

    fun put(pageId: Int, image: WikimediaImage)

    fun clear()
}