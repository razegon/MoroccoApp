package com.rezegon.moroccoapp.data.image

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.rezegon.moroccoapp.data.network.moroccoHttpClient

/**
 * Creates an ImageLoader used for downloading images from Wikimedia Commons.
 *
 * The loader uses the shared HTTP client, which adds the User-Agent
 * required by Wikimedia Commons.
 *
 * It also uses a persistent Coil disk cache, so downloaded image files
 * can remain available after the application process is killed.
 *
 * TODO:
 * Move the ImageLoader to MyApp/Application and share one instance
 * across the whole application.
 */
fun createWikimediaImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)

        // Use the shared HTTP client for Wikimedia requests.
        // This client contains the required User-Agent header.
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = { moroccoHttpClient }
                )
            )
        }

        // Persist downloaded image files on disk.
        //
        // cacheDir belongs to the application, so the cache survives
        // process death but is removed when the app is uninstalled
        // or its app data is cleared.
        .diskCache {
            DiskCache.Builder()
                .directory(
                    context.cacheDir.resolve("wikimedia_image_cache")
                )
                .maxSizeBytes(300L * 1024L * 1024L) // 300 MB
                .build()
        }

        .build()
}
