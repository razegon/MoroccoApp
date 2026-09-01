package com.rezegon.moroccoapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rezegon.moroccoapp.ui.navigation.Screen
import com.rezegon.moroccoapp.ui.screens.city.CitiesScreen
import com.rezegon.moroccoapp.ui.screens.citydetails.CityDetailsScreen
import com.rezegon.moroccoapp.ui.screens.home.HomeScreen
import com.rezegon.moroccoapp.ui.screens.placedetails.PlaceDetailsScreen
import com.rezegon.moroccoapp.ui.screens.places.PlacesScreen

@Composable
fun MoroccoApp() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onCitiesClick = {
                        navController.navigate(Screen.Cities.route)
                    },
                    onPlacesClick = {
                        navController.navigate(Screen.Places.route)
                    }
                )
            }

            composable(Screen.Cities.route) {
                CitiesScreen(
                    onPlaceClick = { place ->
                        navController.navigate("city_details/${place.placeId}")
                    }
                )
            }

            composable(Screen.CityDetails.route) { backStackEntry ->

                val cityId = backStackEntry.arguments?.getString("cityId")
                val placeId = cityId?.toIntOrNull()

                if (placeId != null) {
                    CityDetailsScreen(
                        placeId = placeId,
                        onPlaceClick = { place ->
                            navController.navigate(
                                "place_details/${place.placeId}"
                            )
                        }
                    )
                }
            }

            composable(Screen.Places.route) {
                PlacesScreen(
                    onPlaceClick = { place ->
                        navController.navigate(
                            "place_details/${place.placeId}"
                        )
                    }
                )
            }

            composable(Screen.PlaceDetails.route) { backStackEntry ->

                val placeIdString = backStackEntry.arguments?.getString("placeId")
                val placeId = placeIdString?.toIntOrNull()

                if (placeId != null) {
                    PlaceDetailsScreen(
                        placeId = placeId
                    )
                }
            }
        }
    }
}