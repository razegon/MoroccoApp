package com.razegon.shared.map.marker

import com.razegon.shared.domain.model.PlaceType

fun PlaceType.toMarkerStyle(): MarkerStyle =
    when (this) {
        PlaceType.CITY ->
            MarkerStyle("city", 0xFF596579L)

        PlaceType.MONUMENT ->
            MarkerStyle("monument", 0xFFB85C38L)

        PlaceType.ARCHAEOLOGICAL_SITE ->
            MarkerStyle("monument", 0xFFB85C38L)

        PlaceType.RELIGIOUS_SITE ->
            MarkerStyle("religious_site", 0xFF7956A8L)

        PlaceType.BEACH ->
            MarkerStyle("beach", 0xFF3976B9L)

        PlaceType.MUSEUM ->
            MarkerStyle("museum", 0xFF9670B5L)

        PlaceType.RESTAURANT ->
            MarkerStyle("restaurant", 0xFFD46B35L)

        PlaceType.HOTEL ->
            MarkerStyle("hotel", 0xFF347C79L)

        PlaceType.PARK ->
            MarkerStyle("park", 0xFF438B52L)

        PlaceType.GARDEN ->
            MarkerStyle("park", 0xFF438B52L)

        PlaceType.NATURE ->
            MarkerStyle("nature", 0xFF568B48L)

        PlaceType.VIEWPOINT ->
            MarkerStyle("viewpoint", 0xFF287F9CL)

        PlaceType.SHOP ->
            MarkerStyle("shop", 0xFFA66C36L)

        PlaceType.MARKET ->
            MarkerStyle("market", 0xFFC28B24L)
    }
