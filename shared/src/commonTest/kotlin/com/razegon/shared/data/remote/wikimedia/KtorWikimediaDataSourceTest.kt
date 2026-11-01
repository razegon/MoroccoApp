package com.razegon.shared.data.remote.wikimedia

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.razegon.shared.data.remote.wikimedia.createWikimediaHttpClient

class KtorWikimediaDataSourceTest {

    @Test
    fun getImageMapsResponseCorrectly() = runTest {

        val mockEngine = MockEngine { request ->

            assertEquals(
                "https://commons.wikimedia.org/w/api.php",
                request.url.toString().substringBefore("?")
            )

            assertEquals(
                "MoroccoApp/1.0 (kamilo.tranquilo@gmail.com)",
                request.headers[HttpHeaders.UserAgent]
            )

            respond(
                content = """
                    {
                      "query": {
                        "pages": {
                          "12345": {
                            "title": "File:Test.jpg",
                            "imageinfo": [
                              {
                                "url": "https://upload.wikimedia.org/test.jpg",
                                "width": 1200,
                                "height": 800,
                                "descriptionurl": "https://commons.wikimedia.org/wiki/File:Test.jpg",
                                "extmetadata": {
                                  "Artist": {
                                    "value": "<a href=\"https://example.com\">Test Author</a>"
                                   },
                                  "LicenseShortName": { "value": "CC BY-SA 4.0" },
                                  "LicenseUrl": { "value": "https://creativecommons.org/licenses/by-sa/4.0/" },
                                  "Credit": {
                                    "value": "<span>Test Credit</span>"
                                   }
                                }
                              }
                            ]
                          }
                        }
                      }
                    }
                """.trimIndent(),
                headers = headersOf(
                    HttpHeaders.ContentType,
                    "application/json"
                )
            )
        }

        val httpClient = createWikimediaHttpClient(mockEngine)

        val dataSource = KtorWikimediaDataSource(httpClient)

        val result = dataSource.getImage(12345)

        assertNotNull(result)
        assertEquals("File:Test.jpg", result.title)
        assertEquals("https://upload.wikimedia.org/test.jpg", result.url)
        assertEquals(1200, result.width)
        assertEquals(800, result.height)
        assertEquals("Test Author", result.author)
        assertEquals("CC BY-SA 4.0", result.license)
        assertEquals(
            "https://creativecommons.org/licenses/by-sa/4.0/",
            result.licenseUrl
        )
        assertEquals(
            "https://commons.wikimedia.org/wiki/File:Test.jpg",
            result.filePageUrl
        )
        assertEquals("Test Credit", result.credit)

        httpClient.close()
    }

    @Test
    fun getImageReturnsNullWhenPageDoesNotExist() = runTest {

        val mockEngine = MockEngine {
            respond(
                content = """
                {
                  "query": {
                    "pages": {}
                  }
                }
            """.trimIndent(),
                headers = headersOf(
                    HttpHeaders.ContentType,
                    "application/json"
                )
            )
        }

        val httpClient = createWikimediaHttpClient(mockEngine)
        val dataSource = KtorWikimediaDataSource(httpClient)

        val result = dataSource.getImage(99999)

        assertEquals(null, result)

        httpClient.close()
    }

    @Test
    fun getImageReturnsNullWhenImageInfoIsEmpty() = runTest {

        val mockEngine = MockEngine {
            respond(
                content = """
                {
                  "query": {
                    "pages": {
                      "12345": {
                        "title": "File:Test.jpg",
                        "imageinfo": []
                      }
                    }
                  }
                }
            """.trimIndent(),
                headers = headersOf(
                    HttpHeaders.ContentType,
                    "application/json"
                )
            )
        }

        val httpClient = createWikimediaHttpClient(mockEngine)
        val dataSource = KtorWikimediaDataSource(httpClient)

        val result = dataSource.getImage(12345)

        assertEquals(null, result)

        httpClient.close()
    }
}