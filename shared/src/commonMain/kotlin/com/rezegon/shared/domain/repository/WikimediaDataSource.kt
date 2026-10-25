package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikimediaImage

interface WikimediaDataSource {
    suspend fun getImage(pageId: Int): WikimediaImage?
}