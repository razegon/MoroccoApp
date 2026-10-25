package com.rezegon.moroccoapp.di

import android.content.Context
import com.rezegon.shared.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.moroccoapp.data.api.WikidataRetrofit
import com.rezegon.moroccoapp.data.cache.AppDatabase
import com.rezegon.shared.data.repository.WikidataRepositoryImpl
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache
import com.rezegon.moroccoapp.data.cache.RoomWikidataPersistentCache
import com.rezegon.moroccoapp.data.api.RetrofitWikidataDataSource

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
            dataSource = RetrofitWikidataDataSource(
                WikidataRetrofit.api
            ),
            cache = wikidataCache,
            persistentCache = RoomWikidataPersistentCache(
                database.wikidataDao()
            )

        )
}