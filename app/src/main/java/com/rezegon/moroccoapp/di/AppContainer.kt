package com.rezegon.moroccoapp.di

import android.content.Context
import com.rezegon.shared.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.moroccoapp.data.api.WikidataRetrofit
import com.rezegon.moroccoapp.data.cache.AppDatabase
import com.rezegon.moroccoapp.data.repository.WikidataRepositoryImpl
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache

class AppContainer(
    context: Context,
    private val wikidataCache: WikidataMemoryCache,
    private val database: AppDatabase
) {

    private val applicationContext = context.applicationContext

    private val resourceReader =
        AndroidJsonResourceReader(applicationContext)

    private val dataSource =
        JsonPlaceDataSource(resourceReader)

    val placesRepository =
        PlacesRepositoryImpl(dataSource)

    val wikidataRepository =
        WikidataRepositoryImpl(
            api = WikidataRetrofit.api,
            cache = wikidataCache,
            dao = database.wikidataDao()
        )
}