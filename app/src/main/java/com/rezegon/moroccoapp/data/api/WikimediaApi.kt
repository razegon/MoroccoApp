package com.rezegon.moroccoapp.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WikimediaApi {

    @GET("w/api.php")
    suspend fun searchImages(
        @Query("action") action: String = "query",
        @Query("generator") generator: String = "search",
        @Query("gsrsearch") search: String,
        @Query("gsrnamespace") nameSpace: Int = 6,
        @Query("gsrlimit") searchLimit: Int = 20,
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") iiprop: String = "url|size|extmetadata",
        @Query("format") format: String = "json"
    ): WikimediaResponse

    @GET("w/api.php")
    suspend fun getImage(
        @Query("action") action: String = "query",
        @Query("pageids") pageId: Int,
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") iiprop: String = "url|size|extmetadata",
        @Query("format") format: String = "json"
    ): WikimediaResponse
}