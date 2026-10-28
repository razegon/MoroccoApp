package com.razegon.moroccoapp

import android.app.Application
import com.razegon.moroccoapp.data.image.createWikimediaImageLoader
import com.razegon.moroccoapp.di.AppContainer
import com.razegon.shared.data.cache.WikidataMemoryCache
import com.razegon.shared.data.cache.WikimediaMemoryCache
import com.razegon.shared.data.cache.createAppDatabase
import com.razegon.shared.data.cache.getDatabaseBuilder

class MyApp : Application() {
    val wikidataCache = WikidataMemoryCache()
    val wikimediaCache = WikimediaMemoryCache()

    val database by lazy {
        createAppDatabase(
            getDatabaseBuilder(this)
        )
    }

    val appContainer by lazy {
        AppContainer(
            context = this,
            wikidataCache = wikidataCache,
            wikimediaCache = wikimediaCache,
            database = database
        )
    }

    /**
     * Shared Coil ImageLoader used by all Wikimedia image components.
     *
     * Keeping one instance allows the entire application to share
     * the same memory cache, disk cache and HTTP client.
     */
    val wikimediaImageLoader by lazy {
        createWikimediaImageLoader(this)
    }
}
