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

@Composable
fun MoroccoApp() {

    // Navigation controller manages navigation between screens.
    val navController = rememberNavController()

    // Access the shared application instance and its Room database.
    val app = LocalContext.current.applicationContext as MyApp

    // DAO used to store and read completed quiz results.
    val quizResultDao = app.database.quizResultDao()

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

    // Repository handles saving and loading completed quiz results.
    val quizResultRepository = QuizResultRepositoryImpl(
        dao = quizResultDao
    )

    // Factory creates QuizResultViewModel with the result repository.
    val quizResultFactory = QuizResultViewModelFactory(
        repository = quizResultRepository
    )

    // ViewModel manages saving and loading completed quiz results.
    val quizResultViewModel: QuizResultViewModel = viewModel(
        factory = quizResultFactory
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
                        navController.navigate(Screen.QuizDifficulty.route)
                    },
                    onRankingClick = {
                        navController.navigate(Screen.QuizRanking.route)
                    }
                )
            }

            // Displays the quiz difficulty selection screen.
            composable(Screen.QuizDifficulty.route) {
                QuizDifficultyScreen(
                    onDifficultySelected = { difficulty ->
                        quizViewModel.startQuiz(difficulty)
                        navController.navigate(Screen.QuizGame.route)
                    }
                )
            }

            // Displays the quiz game.
            composable(Screen.QuizGame.route) {
                QuizScreen(
                    viewModel = quizViewModel,
                    onSummaryClick = {
                        navController.navigate(Screen.QuizSummary.route)
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
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onRankingClick = {
                        navController.navigate(Screen.QuizRanking.route)
                    },
                    onHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // Displays the TOP 10 quiz results.
            composable(Screen.QuizRanking.route) {
                QuizRankingScreen(
                    resultViewModel = quizResultViewModel
                )
            }
        }
    }
}

