package com.razegon.moroccoapp

import android.app.Application
import com.razegon.moroccoapp.data.image.createWikimediaImageLoader
import com.razegon.shared.data.cache.createAppDatabase
import com.razegon.shared.data.cache.getDatabaseBuilder
import com.razegon.shared.data.remote.wikimedia.createWikimediaEngine
import com.razegon.shared.di.AppContainer

class MyApp : Application() {

    val database by lazy {
        createAppDatabase(
            getDatabaseBuilder(this)
        )
    }

    val appContainer by lazy {
        AppContainer(
            database = database,
            httpClientEngine = createWikimediaEngine()
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
