package com.rezegon.moroccoapp.di

import android.content.Context
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource

class AppContainer(context: Context) {

    private val applicationContext = context.applicationContext

    private val resourceReader =
        AndroidJsonResourceReader(applicationContext)

    private val dataSource =
        JsonPlaceDataSource(resourceReader)

    val placesRepository =
        PlacesRepositoryImpl(dataSource)
}