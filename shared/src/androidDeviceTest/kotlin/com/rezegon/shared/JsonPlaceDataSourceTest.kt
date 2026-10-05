package com.rezegon.shared

import androidx.test.platform.app.InstrumentationRegistry
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.shared.domain.model.PlaceType
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonPlaceDataSourceTest {

    @Test
    fun readsMarrakeshFromJson() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        println(context.assets.list("")?.joinToString())

        val reader = AndroidJsonResourceReader(context)
        val dataSource = JsonPlaceDataSource(reader)

        val places = dataSource.getPlaces()

        println(">>> LICZBA PLACES: ${places.size}")
        places.forEach {
            println(">>> ${it.place.placeId} - ${it.content.name}")
        }

        assertEquals(41, places.size)

        val marrakesh = places.first()
        val fes = places.first { it.place.placeId == 4102 }

        assertEquals(4100, marrakesh.place.placeId)
        assertEquals("Marrakesz", marrakesh.content.name)
        assertEquals(PlaceType.CITY, marrakesh.place.placeType)
        assertEquals("Q101625", marrakesh.place.wikidataId)

        assertEquals(4102, fes.place.placeId)
        assertEquals("Fez", fes.content.name)
        assertEquals(PlaceType.CITY, fes.place.placeType)
        assertEquals("Q80985", fes.place.wikidataId)
    }
}