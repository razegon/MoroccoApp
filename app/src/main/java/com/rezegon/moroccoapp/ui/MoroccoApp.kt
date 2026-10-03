package com.rezegon.moroccoapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.quiz.QuizQuestionDataSource
import com.rezegon.moroccoapp.data.repository.QuizRepositoryImpl
import com.rezegon.moroccoapp.data.repository.QuizResultRepositoryImpl
import com.rezegon.moroccoapp.ui.navigation.Screen
import com.rezegon.moroccoapp.ui.screens.city.CitiesScreen
import com.rezegon.moroccoapp.ui.screens.citydetails.CityDetailsScreen
import com.rezegon.moroccoapp.ui.screens.home.HomeScreen
import com.rezegon.moroccoapp.ui.screens.placedetails.PlaceDetailsScreen
import com.rezegon.moroccoapp.ui.screens.places.PlacesScreen
import com.rezegon.moroccoapp.ui.screens.quiz.QuizDifficultyScreen
import com.rezegon.moroccoapp.ui.screens.quiz.QuizRankingScreen
import com.rezegon.moroccoapp.ui.screens.quiz.QuizScreen
import com.rezegon.moroccoapp.ui.screens.quiz.QuizStartScreen
import com.rezegon.moroccoapp.ui.screens.quiz.QuizSummaryScreen
import com.rezegon.moroccoapp.viewmodel.QuizResultViewModel
import com.rezegon.moroccoapp.viewmodel.QuizResultViewModelFactory
import com.rezegon.moroccoapp.viewmodel.QuizViewModel
import com.rezegon.moroccoapp.viewmodel.QuizViewModelFactory
import com.rezegon.moroccoapp.viewmodel.ArcadeViewModel
import com.rezegon.moroccoapp.viewmodel.ArcadeViewModelFactory
import androidx.compose.runtime.LaunchedEffect
import com.rezegon.moroccoapp.ui.screens.arcade.ArcadeScreen
import com.rezegon.moroccoapp.data.repository.ArcadeResultRepositoryImpl
import com.rezegon.moroccoapp.ui.screens.arcade.ArcadeRankingScreen
import com.rezegon.moroccoapp.viewmodel.ArcadeResultViewModel
import com.rezegon.moroccoapp.viewmodel.ArcadeResultViewModelFactory

@Composable
fun MoroccoApp() {

    // Navigation controller manages navigation between screens.
    val navController = rememberNavController()

    // Access the shared application instance and its Room database.
    val app = LocalContext.current.applicationContext as MyApp

    // DAO used to store and read completed quiz results.
    val quizResultDao = app.database.quizResultDao()

    // DAO used to store and read completed arcade quiz results.
    val arcadeResultDao = app.database.arcadeResultDao()

    // Shared quiz dependencies used by the quiz and summary screens.
    val quizDataSource = QuizQuestionDataSource(app)
    val quizRepository = QuizRepositoryImpl(quizDataSource)

    // Factory creates QuizViewModel with the required repository.
    val quizFactory = QuizViewModelFactory(
        quizRepository = quizRepository
    )

    // Shared ViewModel used by both QuizScreen and QuizSummaryScreen
    // so that the quiz state and final score are preserved.
    val quizViewModel: QuizViewModel = viewModel(
        factory = quizFactory
    )

    // Factory creates ArcadeViewModel with the shared quiz repository.
    val arcadeFactory = ArcadeViewModelFactory(
        repository = quizRepository
    )

// ViewModel manages the state and logic of the Arcade game.
    val arcadeViewModel: ArcadeViewModel = viewModel(
        factory = arcadeFactory
    )

    // Repository handles saving and loading completed quiz results.
    val quizResultRepository = QuizResultRepositoryImpl(
        dao = quizResultDao
    )

    // Repository handles saving and loading completed arcade results.
    val arcadeResultRepository = ArcadeResultRepositoryImpl(
        dao = arcadeResultDao
    )

    // Factory creates QuizResultViewModel with the result repository.
    val quizResultFactory = QuizResultViewModelFactory(
        repository = quizResultRepository
    )

    // Factory creates ArcadeResultViewModel with the result repository.
    val arcadeResultFactory = ArcadeResultViewModelFactory(
        repository = arcadeResultRepository
    )

    // ViewModel manages saving and loading completed quiz results.
    val quizResultViewModel: QuizResultViewModel = viewModel(
        factory = quizResultFactory
    )

    // ViewModel manages saving and loading completed arcade results.
    val arcadeResultViewModel: ArcadeResultViewModel = viewModel(
        factory = arcadeResultFactory
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        // Defines all application routes and the screen
        // that should be displayed for each route.
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // Home screen is the starting destination of the application.
            composable(Screen.Home.route) {
                HomeScreen(
                    // Navigate to the list of cities.
                    onCitiesClick = {
                        navController.navigate(Screen.Cities.route)
                    },
                    // Navigate to the list of places.
                    onPlacesClick = {
                        navController.navigate(Screen.Places.route)
                    },
                    // Navigate to the Quiz.
                    onQuizClick = {
                        navController.navigate(Screen.Quiz.route)
                    }
                )
            }

            // Displays the list of cities.
            composable(Screen.Cities.route) {
                CitiesScreen(
                    // Pass the selected city's ID (placeId) to CityDetailsScreen.
                    onPlaceClick = { place ->
                        navController.navigate("city_details/${place.placeId}")
                    }
                )
            }

            // Displays details for the selected city.
            // The city ID is read from the navigation route.
            composable(Screen.CityDetails.route) { backStackEntry ->

                val cityId = backStackEntry.arguments?.getString("cityId")
                val placeId = cityId?.toIntOrNull()

                // Open the screen only when a valid city ID was received.
                if (placeId != null) {
                    CityDetailsScreen(
                        placeId = placeId,

                        // Navigate from a city to details of one of its places.
                        onPlaceClick = { place ->
                            navController.navigate(
                                "place_details/${place.placeId}"
                            )
                        }
                    )
                }
            }

            // Displays the list of places.
            composable(Screen.Places.route) {
                PlacesScreen(
                    // Pass the selected place ID to PlaceDetailsScreen.
                    onPlaceClick = { place ->
                        navController.navigate(
                            "place_details/${place.placeId}"
                        )
                    }
                )
            }

            // Displays details for the selected place.
            // The place ID is read from the navigation route.
            composable(Screen.PlaceDetails.route) { backStackEntry ->

                val placeIdString =
                    backStackEntry.arguments?.getString("placeId")
                val placeId = placeIdString?.toIntOrNull()

                // Open the screen only when a valid place ID was received.
                if (placeId != null) {
                    PlaceDetailsScreen(
                        placeId = placeId
                    )
                }
            }

            // Displays the quiz start screen.
            composable(Screen.Quiz.route) {
                QuizStartScreen(
                    onStartQuizClick = {
                        quizViewModel.restartQuiz()
                        navController.navigate(Screen.QuizDifficulty.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
                            }
                        }
                    },

                    onArcadeClick = {
                        arcadeViewModel.startGame()
                        navController.navigate(Screen.QuizArcade.route)
                    },

                    onRankingClick = {
                        navController.navigate(Screen.QuizRanking.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // Displays the Arcade quiz.
            composable(Screen.QuizArcade.route) {

                ArcadeScreen(
                    viewModel = arcadeViewModel,
                    resultViewModel = arcadeResultViewModel,
                    onRankingClick = {
                        navController.navigate(Screen.QuizArcadeRanking.route)
                    },
                    onQuizMenuClick = {
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onExitClick = {
                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            // Displays the TOP 10 Arcade results.
            composable(Screen.QuizArcadeRanking.route) {
                ArcadeRankingScreen(
                    resultViewModel = arcadeResultViewModel
                )
            }

            // Displays the quiz difficulty selection screen.
            composable(Screen.QuizDifficulty.route) {
                QuizDifficultyScreen(
                    onDifficultySelected = { difficulty ->
                        quizViewModel.startQuiz(difficulty)
                        navController.navigate(Screen.QuizGame.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // Displays the quiz game.
            composable(Screen.QuizGame.route) {
                QuizScreen(
                    viewModel = quizViewModel,
                    onSummaryClick = {
                        navController.navigate(Screen.QuizSummary.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onExitClick = {
                        quizViewModel.restartQuiz()

                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            // Displays the quiz summary screen.
            composable(Screen.QuizSummary.route) {
                QuizSummaryScreen(
                    viewModel = quizViewModel,
                    resultViewModel = quizResultViewModel,
                    onRestartClick = {
                        quizViewModel.restartQuiz()

                        navController.navigate(Screen.Quiz.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onRankingClick = {
                        navController.navigate(Screen.QuizRanking.route) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = false
                            }
                        }
                    },
                    onHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = false
                            }
                        }
                    },
                )
            }

            // Displays the TOP 10 quiz results.
            composable(Screen.QuizRanking.route) {
                QuizRankingScreen(
                    resultViewModel = quizResultViewModel,
                )
            }
        }
    }
}

