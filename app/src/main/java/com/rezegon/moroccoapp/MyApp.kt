package com.rezegon.moroccoapp

import android.app.Application
import com.rezegon.moroccoapp.data.image.createWikimediaImageLoader
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.data.cache.WikidataMemoryCache
import com.rezegon.shared.data.cache.WikimediaMemoryCache
import com.rezegon.shared.data.cache.createAppDatabase
import com.rezegon.shared.data.cache.getDatabaseBuilder

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
