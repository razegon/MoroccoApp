package com.rezegon.shared

import androidx.test.platform.app.InstrumentationRegistry
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource
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

        assertEquals(1, places.size)

        val marrakesh = places.first()

        assertEquals(4100, marrakesh.place.placeId)
        assertEquals("Marrakesz", marrakesh.content.name)
        assertEquals("Q101625", marrakesh.place.wikidataId)
    }
}