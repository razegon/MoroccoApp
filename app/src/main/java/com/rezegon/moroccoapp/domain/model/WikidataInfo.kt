package com.rezegon.moroccoapp.domain.model

data class WikidataInfo(
    val wikidataId: String?,
    val population: Long?,
    val area: Double?,
    val elevation: Double?,
    val inception: WikidataTime?,
)

data class WikidataTime(
    val year: Int?,
    val precision: Int?,
    val raw: String?
)

