package com.razegon.moroccoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.ui.screens.arcade.ArcadeScreen
import com.razegon.moroccoapp.ui.screens.city.CitiesScreen
import com.razegon.moroccoapp.ui.screens.citydetails.CityDetailsScreen
import com.razegon.shared.ui.screens.map.FullscreenCityMapScreen
import com.razegon.moroccoapp.ui.screens.placedetails.PlaceDetailsScreen
import com.razegon.moroccoapp.ui.screens.places.PlacesScreen
import com.razegon.moroccoapp.ui.screens.quiz.QuizScreen
import com.razegon.moroccoapp.ui.screens.quiz.QuizSummaryScreen
import com.razegon.moroccoapp.ui.screens.rankingquizarcade.ArcadeRankingScreen
import com.razegon.moroccoapp.ui.screens.rankingquizarcade.QuizRankingScreen
import com.razegon.moroccoapp.viewmodel.ArcadeAndroidViewModel
import com.razegon.moroccoapp.viewmodel.ArcadeResultAndroidViewModel
import com.razegon.moroccoapp.viewmodel.ArcadeResultViewModelFactory
import com.razegon.moroccoapp.viewmodel.ArcadeViewModelFactory
import com.razegon.moroccoapp.viewmodel.CityMapAndroidViewModel
import com.razegon.moroccoapp.viewmodel.CityMapViewModelFactory
import com.razegon.moroccoapp.viewmodel.QuizAndroidViewModel
import com.razegon.moroccoapp.viewmodel.QuizResultAndroidViewModel
import com.razegon.moroccoapp.viewmodel.QuizResultViewModelFactory
import com.razegon.moroccoapp.viewmodel.QuizViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.ui.MoroccoApp

@Composable
fun AndroidMoroccoApp() {

    val app = LocalContext.current.applicationContext as MyApp
    val appContainer = app.appContainer

    val quizViewModel: QuizAndroidViewModel = viewModel(
        factory = QuizViewModelFactory(appContainer)
    )

    val arcadeViewModel: ArcadeAndroidViewModel = viewModel(
        factory = ArcadeViewModelFactory(appContainer)
    )

    val quizResultViewModel: QuizResultAndroidViewModel = viewModel(
        factory = QuizResultViewModelFactory(
            repository = appContainer.quizResultRepository
        )
    )

    val arcadeResultViewModel: ArcadeResultAndroidViewModel = viewModel(
        factory = ArcadeResultViewModelFactory(
            repository = appContainer.arcadeResultRepository
        )
    )

    MoroccoApp(
        onStartQuiz = {
            quizViewModel.restartQuiz()
        },
        onDifficultySelected = { difficulty: QuizDifficulty ->
            quizViewModel.startQuiz(difficulty)
        },
        onStartArcade = {
            arcadeViewModel.startGame()
        },

        citiesScreen = { onPlaceClick: (PlaceData) -> Unit ->
            CitiesScreen(
                onPlaceClick = onPlaceClick
            )
        },

        cityDetailsScreen = { placeId, selectedPlaceId, onPlaceSelected, onPlaceClick, onOpenCityMap ->
            CityDetailsScreen(
                placeId = placeId,
                selectedPlaceId = selectedPlaceId,
                onPlaceSelected = onPlaceSelected,
                onPlaceClick = onPlaceClick,
                onOpenCityMap = onOpenCityMap
            )
        },

        cityMapScreen = { cityId, selectedPlaceId, onPlaceSelected, onPlaceClick, onBackClick ->

            val cityMapViewModel: CityMapAndroidViewModel = viewModel(
                key = "cityMap_$cityId",
                factory = CityMapViewModelFactory(
                    cityId = cityId,
                    appContainer = appContainer
                )
            )

            val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
                key = "wikimediaCityMap_$cityId",
                factory = WikimediaViewModelFactory(
                    appContainer.wikimediaRepository
                )
            )

            val uiState by cityMapViewModel.uiState.collectAsState()
            val wikimediaImages by wikimediaViewModel.images.collectAsState()

            LaunchedEffect(uiState.placeData, uiState.children) {
                uiState.placeData?.place?.wikimediaImages?.forEach { imageRef ->
                    imageRef?.let {
                        wikimediaViewModel.getImage(it.pageId)
                    }
                }

                uiState.children.forEach { child ->
                    child.place.wikimediaImages
                        .firstOrNull()
                        ?.let { imageRef ->
                            wikimediaViewModel.getImage(imageRef.pageId)
                        }
                }
            }

            FullscreenCityMapScreen(
                uiState = uiState,
                onPlaceClick = onPlaceClick,
                onBackClick = onBackClick,
                selectedPlaceId = selectedPlaceId,
                onPlaceSelected = onPlaceSelected,
                wikimediaImages = wikimediaImages,
                imageLoader = app.wikimediaImageLoader
            )
        },

        placesScreen = { onPlaceClick: (PlaceData) -> Unit ->
            PlacesScreen(
                onPlaceClick = onPlaceClick
            )
        },

        placeDetailsScreen = { placeId: Int, onOpenMap: (Double, Double, Float) -> Unit ->
            PlaceDetailsScreen(
                placeId = placeId,
                onOpenMap = onOpenMap,
            )
        },

        quizGameScreen = { onSummaryClick: () -> Unit, onExitClick: () -> Unit ->
            QuizScreen(
                viewModel = quizViewModel,
                onSummaryClick = onSummaryClick,
                onExitClick = onExitClick
            )
        },

        quizSummaryScreen = {
                onRestartClick: () -> Unit,
                onRankingClick: () -> Unit,
                onHomeClick: () -> Unit ->
            QuizSummaryScreen(
                viewModel = quizViewModel,
                resultViewModel = quizResultViewModel,
                onRestartClick = onRestartClick,
                onRankingClick = onRankingClick,
                onHomeClick = onHomeClick
            )
        },

        quizRankingScreen = { onBackClick: () -> Unit ->
            QuizRankingScreen(
                resultViewModel = quizResultViewModel,
                onBackClick = onBackClick
            )
        },

        arcadeGameScreen = {
                onRankingClick: () -> Unit,
                onQuizMenuClick: () -> Unit,
                onExitClick: () -> Unit ->
            ArcadeScreen(
                viewModel = arcadeViewModel,
                resultViewModel = arcadeResultViewModel,
                onRankingClick = onRankingClick,
                onQuizMenuClick = onQuizMenuClick,
                onExitClick = onExitClick
            )
        },

        arcadeRankingScreen = { onBackClick: () -> Unit ->
            ArcadeRankingScreen(
                resultViewModel = arcadeResultViewModel,
                onBackClick = onBackClick
            )
        }
    )
}
