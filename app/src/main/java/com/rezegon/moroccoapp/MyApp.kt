package com.rezegon.moroccoapp

import android.app.Application
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache
import com.rezegon.moroccoapp.data.cache.WikimediaMemoryCache

class MyApp : Application() {

    val wikidataCache = WikidataMemoryCache()
    val wikimediaCache = WikimediaMemoryCache()

}
