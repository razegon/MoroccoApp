package com.rezegon.moroccoapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface WikidataApi {

    @GET("/wiki/Special:EntityData/{id}.json")
    suspend fun getEntity(
        @Path("id") id: String
    ): Map<String, Any>
}