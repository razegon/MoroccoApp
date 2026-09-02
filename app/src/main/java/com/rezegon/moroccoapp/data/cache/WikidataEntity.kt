package com.rezegon.moroccoapp.data.cache

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class WikidataEntity(
    @PrimaryKey
    val wikidataId: String,
    val population: Long?,
    val area: Double?,
    val elevation: Double?,
    val inceptionYear: Int?,
    val inceptionPrecision: Int?,
    val inceptionRaw: String?
)