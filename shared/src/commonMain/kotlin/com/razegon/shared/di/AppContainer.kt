package com.razegon.shared.di

import com.razegon.shared.data.cache.AppDatabase
import com.razegon.shared.data.cache.RoomWikidataPersistentCache
import com.razegon.shared.data.cache.RoomWikimediaPersistentCache
import com.razegon.shared.data.cache.WikidataMemoryCache
import com.razegon.shared.data.cache.WikimediaMemoryCache
import com.razegon.shared.data.quiz.JsonQuizQuestionDataSource
import com.razegon.shared.data.remote.wikidata.KtorWikidataDataSource
import com.razegon.shared.data.remote.wikimedia.KtorWikimediaDataSource
import com.razegon.shared.data.remote.wikimedia.createWikimediaHttpClient
import com.razegon.shared.data.repository.ArcadeResultRepositoryImpl
import com.razegon.shared.data.repository.PlacesRepositoryImpl
import com.razegon.shared.data.repository.QuizRepositoryImpl
import com.razegon.shared.data.repository.QuizResultRepositoryImpl
import com.razegon.shared.data.repository.WikidataRepositoryImpl
import com.razegon.shared.data.repository.WikimediaRepositoryImpl
import com.razegon.shared.data.source.ComposeJsonResourceReader
import com.razegon.shared.data.source.JsonPlaceDataSource
import io.ktor.client.engine.HttpClientEngine

// Shared dependency container for repositories and data sources.
class AppContainer(
    database: AppDatabase,
    httpClientEngine: HttpClientEngine
) {
    // In-memory caches for Wikidata and Wikimedia responses.
    private val wikidataCache = WikidataMemoryCache()
    private val wikimediaCache = WikimediaMemoryCache()

    // Shared HTTP client used by both remote data sources.
    private val wikimediaHttpClient = createWikimediaHttpClient(
        engine = httpClientEngine
    )

    // Shared JSON resource reader for bundled application data.
    private val resourceReader = ComposeJsonResourceReader()

    // Data source and repository for place information.
    private val placesDataSource = JsonPlaceDataSource(resourceReader)

    val placesRepository = PlacesRepositoryImpl(placesDataSource)

    // Repository for Wikidata information with memory and persistent caching.
    val wikidataRepository = WikidataRepositoryImpl(
        dataSource = KtorWikidataDataSource(
            httpClient = wikimediaHttpClient
        ),
        cache = wikidataCache,
        persistentCache = RoomWikidataPersistentCache(
            database.wikidataDao()
        )
    )

    // Repository for Wikimedia images with memory and persistent caching.
    val wikimediaRepository = WikimediaRepositoryImpl(
        dataSource = KtorWikimediaDataSource(
            httpClient = wikimediaHttpClient
        ),
        cache = wikimediaCache,
        persistentCache = RoomWikimediaPersistentCache(
            database.wikimediaDao()
        )
    )

    // Repository for quiz questions loaded from shared Compose resources.
    val quizRepository = QuizRepositoryImpl(
        dataSource = JsonQuizQuestionDataSource(
            resourceReader = resourceReader
        )
    )

    // Repositories for locally stored game results.
    val quizResultRepository = QuizResultRepositoryImpl(
        dao = database.quizResultDao()
    )

    val arcadeResultRepository = ArcadeResultRepositoryImpl(
        dao = database.arcadeResultDao()
    )
}