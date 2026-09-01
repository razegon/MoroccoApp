package com.rezegon.moroccoapp.ui.navigation

sealed class Screen(
    val route: String
) {
    data object Home : Screen("home")

    data object Cities : Screen("cities")

    data object CityDetails : Screen("city_details/{cityId}")

    data object Places : Screen("places")

    data object PlaceDetails : Screen("place_details/{placeId}")
}