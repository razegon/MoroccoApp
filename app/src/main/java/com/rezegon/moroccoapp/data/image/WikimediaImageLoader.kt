package com.rezegon.moroccoapp.data.image

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.rezegon.moroccoapp.data.network.moroccoHttpClient

/**
 * Wspólny klient HTTP używany do pobierania zdjęć Wikimedia Commons.
 *
 * Wikimedia wymaga identyfikacji klienta poprzez nagłówek User-Agent.
 * Jego brak może powodować odpowiedź HTTP 403 przy pobieraniu zdjęć.
 *
 * TODO:
 * Po utworzeniu klasy MyApp/Application przenieść konfigurację
 * klienta i ImageLoader do globalnej konfiguracji aplikacji.
 */

/**
 * Tworzy ImageLoader używany do pobierania zdjęć z Wikimedia Commons.
 *
 * Obecnie loader jest tworzony lokalnie w komponentach Compose.
 * Docelowo zostanie przeniesiony do Application/MyApp, gdy dodamy
 * globalną konfigurację aplikacji (m.in. mapy OSM).
 */
fun createWikimediaImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = { moroccoHttpClient }
                )
            )
        }
        .build()
}
