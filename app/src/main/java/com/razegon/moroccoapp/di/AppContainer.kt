package com.razegon.moroccoapp.di

import android.content.Context
import com.razegon.moroccoapp.data.api.RetrofitWikidataDataSource
import com.razegon.moroccoapp.data.api.WikidataRetrofit
import com.razegon.shared.data.cache.AppDatabase
import com.razegon.shared.data.cache.RoomWikidataPersistentCache
import com.razegon.shared.data.cache.RoomWikimediaPersistentCache
import com.razegon.shared.data.cache.WikidataMemoryCache
import com.razegon.shared.data.cache.WikimediaMemoryCache
import com.razegon.shared.data.quiz.JsonQuizQuestionDataSource
import com.razegon.shared.data.remote.wikimedia.KtorWikimediaDataSource
import com.razegon.shared.data.remote.wikimedia.createWikimediaEngine
import com.razegon.shared.data.remote.wikimedia.createWikimediaHttpClient
import com.razegon.shared.data.repository.PlacesRepositoryImpl
import com.razegon.shared.data.repository.QuizRepositoryImpl
import com.razegon.shared.data.repository.WikidataRepositoryImpl
import com.razegon.shared.data.repository.WikimediaRepositoryImpl
import com.razegon.shared.data.source.ComposeJsonResourceReader
import com.razegon.shared.data.source.JsonPlaceDataSource

class AppContainer(
    context: Context,
    private val wikidataCache: WikidataMemoryCache,
    private val wikimediaCache: WikimediaMemoryCache,
    private val database: AppDatabase
) {

    private val wikimediaHttpClient =
        createWikimediaHttpClient(
            engine = createWikimediaEngine()
        )

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
            dataSource = KtorWikimediaDataSource(
                httpClient = wikimediaHttpClient
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