package com.rezegon.moroccoapp

import android.app.Application
import androidx.room3.Room
import com.rezegon.moroccoapp.data.cache.AppDatabase
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache
import com.rezegon.moroccoapp.data.cache.WikimediaMemoryCache
import com.rezegon.moroccoapp.data.image.createWikimediaImageLoader
import kotlin.getValue
import kotlin.lazy

class MyApp : Application() {

    val wikidataCache = WikidataMemoryCache()
    val wikimediaCache = WikimediaMemoryCache()

    val database by lazy {
        Room.databaseBuilder<AppDatabase>(
            applicationContext,
            "morocco_app_db"
        ).build()
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
