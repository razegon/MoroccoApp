package com.razegon.shared.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.navigation.Screen
import com.razegon.shared.ui.screens.arcade.ArcadeInstructionsScreen
import com.razegon.shared.ui.screens.home.HomeScreen
import com.razegon.shared.ui.screens.quiz.QuizDifficultyScreen
import com.razegon.shared.ui.screens.quiz.QuizStartScreen
import com.razegon.shared.ui.screens.placedetails.PlaceMapSection

// Shared application entry point and navigation graph for the common UI.
@Composable
fun MoroccoApp(
    onStartQuiz: () -> Unit,
    onDifficultySelected: (QuizDifficulty) -> Unit,
    onStartArcade: () -> Unit,

    citiesScreen: @Composable ((PlaceData) -> Unit) -> Unit,
    cityDetailsScreen: @Composable (Int, (PlaceData) -> Unit) -> Unit,
    placesScreen: @Composable ((PlaceData) -> Unit) -> Unit,
    placeDetailsScreen: @Composable (
        Int,
        (Double, Double, Float) -> Unit
    ) -> Unit,

    quizGameScreen: @Composable (() -> Unit, () -> Unit) -> Unit,
    quizSummaryScreen: @Composable (() -> Unit, () -> Unit, () -> Unit) -> Unit,
    quizRankingScreen: @Composable (() -> Unit) -> Unit,

    arcadeGameScreen: @Composable (() -> Unit, () -> Unit, () -> Unit) -> Unit,
    arcadeRankingScreen: @Composable (() -> Unit) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        )
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            composable(Screen.Home.route) {
                HomeScreen(
                    modifier = Modifier,
                    onCitiesClick = {
                        navController.navigate(Screen.Cities.route)
                    },
                    onPlacesClick = {
                        navController.navigate(Screen.Places.route)
                    },
                    onQuizClick = {
                        navController.navigate(Screen.Quiz.route)
                    }
                )
            }

            composable(Screen.Cities.route) {
                citiesScreen { place ->
                    navController.navigate(
                        "city_details/${place.place.placeId}"
                    )
                }
            }

            composable(Screen.CityDetails.route) { backStackEntry ->
                val cityId = backStackEntry.arguments?.read {
                    getString("cityId")
                }?.toIntOrNull()

                if (cityId != null) {
                    cityDetailsScreen(cityId) { placeData ->
                        navController.navigate(
                            "place_details/${placeData.place.placeId}"
                        )
                    }
                }
            }

            composable(Screen.Places.route) {
                placesScreen { placeData ->
                    navController.navigate(
                        "place_details/${placeData.place.placeId}"
                    )
                }
            }

            composable(Screen.PlaceDetails.route) { backStackEntry ->
                val placeId = backStackEntry.arguments?.read {
                    getString("placeId")
                }?.toIntOrNull()

                if (placeId != null) {
                    placeDetailsScreen(placeId) { latitude, longitude, zoom ->
                        navController.navigate(
                            "place_map/$latitude/$longitude/$zoom"
                        )
                    }
                }
            }

            composable(Screen.PlaceMapFullScreen.route) { backStackEntry ->

                val latitude = backStackEntry.arguments
                    ?.read { getString("latitude") }
                    ?.toDoubleOrNull()

                val longitude = backStackEntry.arguments
                    ?.read { getString("longitude") }
                    ?.toDoubleOrNull()

                val zoom = backStackEntry.arguments
                    ?.read { getString("zoom") }
                    ?.toFloatOrNull()

                if (latitude != null && longitude != null) {
                    PlaceMapSection(
                        latitude = latitude,
                        longitude = longitude,
                        zoom = zoom ?: 12f,
                        modifier = Modifier.fillMaxSize(),
                        onExpandClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            // Keep the quiz menu as the entry point for the entire quiz flow.
            composable(Screen.Quiz.route) {
                QuizStartScreen(
                    onStartQuizClick = {
                        onStartQuiz()

                        navController.navigate(Screen.QuizDifficulty.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onStartArcadeClick = {
                        navController.navigate(Screen.ArcadeInstructions.route)
                    },
                    onRankingClick = {
                        navController.navigate(Screen.QuizRanking.route)
                    },
                    onArcadeRankingClick = {
                        navController.navigate(Screen.QuizArcadeRanking.route)
                    }
                )
            }

            composable(Screen.QuizDifficulty.route) {
                QuizDifficultyScreen(
                    onDifficultySelected = { difficulty ->
                        onDifficultySelected(difficulty)

                        // Remove the difficulty screen while keeping the quiz menu.
                        navController.navigate(Screen.QuizGame.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.QuizGame.route) {
                quizGameScreen(
                    {
                        // Remove the game screen and keep the quiz menu below the summary.
                        navController.navigate(Screen.QuizSummary.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    {
                        onStartQuiz()
                        navController.popBackStack(
                            Screen.Quiz.route,
                            inclusive = false
                        )
                    }
                )
            }

            composable(Screen.QuizSummary.route) {
                quizSummaryScreen(
                    {
                        // Reset the quiz and return to the existing quiz menu.
                        onStartQuiz()
                        navController.popBackStack(
                            Screen.Quiz.route,
                            inclusive = false
                        )
                    },
                    {
                        navController.navigate(Screen.QuizRanking.route)
                    },
                    {
                        navController.popBackStack(
                            Screen.Home.route,
                            inclusive = false
                        )
                    }
                )
            }

            composable(Screen.QuizRanking.route) {
                quizRankingScreen {
                    navController.popBackStack(
                        Screen.Quiz.route,
                        inclusive = false
                    )
                }
            }

            composable(Screen.ArcadeInstructions.route) {
                ArcadeInstructionsScreen(
                    onStartClick = {
                        onStartArcade()

                        navController.navigate(Screen.QuizArcade.route) {
                            popUpTo(Screen.ArcadeInstructions.route) {
                                inclusive = true
                            }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack(
                            Screen.Quiz.route,
                            inclusive = false
                        )
                    }
                )
            }

            composable(Screen.QuizArcade.route) {
                arcadeGameScreen(
                    {
                        navController.navigate(Screen.QuizArcadeRanking.route)
                    },
                    {
                        navController.popBackStack(
                            Screen.Quiz.route,
                            inclusive = false
                        )
                    },
                    {
                        navController.popBackStack(
                            Screen.Quiz.route,
                            inclusive = false
                        )
                    }
                )
            }

            composable(Screen.QuizArcadeRanking.route) {
                arcadeRankingScreen {
                    navController.popBackStack(
                        Screen.Quiz.route,
                        inclusive = false
                    )
                }
            }
        }
    }
}