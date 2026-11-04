
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
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.navigation.Screen
import com.razegon.shared.ui.screens.arcade.ArcadeInstructionsScreen
import com.razegon.shared.ui.screens.home.HomeScreen
import com.razegon.shared.ui.screens.quiz.QuizDifficultyScreen
import com.razegon.shared.ui.screens.quiz.QuizStartScreen
import androidx.savedstate.read

@Composable
fun MoroccoApp(
    onStartQuiz: () -> Unit,
    onDifficultySelected: (QuizDifficulty) -> Unit,
    onStartArcade: () -> Unit,

    citiesScreen: @Composable ((PlaceData) -> Unit) -> Unit,
    cityDetailsScreen: @Composable (Int, (PlaceData) -> Unit) -> Unit,
    placesScreen: @Composable ((PlaceData) -> Unit) -> Unit,
    placeDetailsScreen: @Composable (Int) -> Unit,

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
            modifier = Modifier.padding(innerPadding)
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
                    placeDetailsScreen(placeId)
                }
            }

            composable(Screen.Quiz.route) {
                QuizStartScreen(
                    onStartQuizClick = {
                        onStartQuiz()
                        navController.navigate(Screen.QuizDifficulty.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
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

                        navController.navigate(Screen.QuizGame.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
                            }
                        }
                    },
                    onBackClick = {
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.QuizGame.route) {
                quizGameScreen(
                    {
                        navController.navigate(Screen.QuizSummary.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    {
                        onStartQuiz()
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(Screen.QuizSummary.route) {
                quizSummaryScreen(
                    {
                        onStartQuiz()
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    {
                        navController.navigate(Screen.QuizRanking.route)
                    },
                    {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(Screen.QuizRanking.route) {
                quizRankingScreen {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
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
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.QuizArcade.route) {
                arcadeGameScreen(
                    {
                        navController.navigate(Screen.QuizArcadeRanking.route)
                    },
                    {
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    {
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(Screen.QuizArcadeRanking.route) {
                arcadeRankingScreen {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

