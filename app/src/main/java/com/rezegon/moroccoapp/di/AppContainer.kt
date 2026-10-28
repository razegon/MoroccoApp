package com.rezegon.moroccoapp.di

import android.content.Context
import com.rezegon.shared.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.moroccoapp.data.api.WikidataRetrofit
import com.rezegon.shared.data.cache.AppDatabase
import com.rezegon.shared.data.repository.WikidataRepositoryImpl
import com.rezegon.shared.data.cache.WikidataMemoryCache
import com.rezegon.shared.data.cache.RoomWikidataPersistentCache
import com.rezegon.moroccoapp.data.api.RetrofitWikidataDataSource
import com.rezegon.moroccoapp.data.api.RetrofitWikimediaDataSource
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.shared.data.cache.RoomWikimediaPersistentCache
import com.rezegon.shared.data.cache.WikimediaMemoryCache
import com.rezegon.shared.data.repository.WikimediaRepositoryImpl
import com.rezegon.shared.data.quiz.JsonQuizQuestionDataSource
import com.rezegon.shared.data.source.ComposeJsonResourceReader
import com.rezegon.shared.data.repository.QuizRepositoryImpl

class AppContainer(
    context: Context,
    private val wikidataCache: WikidataMemoryCache,
    private val wikimediaCache: WikimediaMemoryCache,
    private val database: AppDatabase
) {

    private val applicationContext = context.applicationContext

    private val resourceReader =
        ComposeJsonResourceReader()

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

    val wikimediaRepository =
        WikimediaRepositoryImpl(
            dataSource = RetrofitWikimediaDataSource(
                WikimediaRetrofit.api
            ),
            cache = wikimediaCache,
            persistentCache = RoomWikimediaPersistentCache(
                database.wikimediaDao()
            )
        )

    val quizRepository = QuizRepositoryImpl(
        dataSource = JsonQuizQuestionDataSource(
            resourceReader = ComposeJsonResourceReader()
        )
    )
}