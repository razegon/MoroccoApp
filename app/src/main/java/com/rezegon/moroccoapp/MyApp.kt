package com.rezegon.moroccoapp

import android.app.Application
import androidx.room3.Room
import com.rezegon.moroccoapp.data.cache.AppDatabase
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache
import com.rezegon.moroccoapp.data.cache.WikimediaMemoryCache
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
}
