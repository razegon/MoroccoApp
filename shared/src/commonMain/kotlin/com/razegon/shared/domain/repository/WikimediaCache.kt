package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikimediaImage

interface WikimediaCache {

    fun get(pageId: Int): WikimediaImage?

    fun put(pageId: Int, image: WikimediaImage)

    fun clear()
}