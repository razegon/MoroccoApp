package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.WikimediaImage

interface WikimediaRepository {

    suspend fun getImage(pageId: Int): WikimediaImage?

}