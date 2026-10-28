package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikimediaImage

interface WikimediaRepository {

    suspend fun getImage(pageId: Int): WikimediaImage?

}