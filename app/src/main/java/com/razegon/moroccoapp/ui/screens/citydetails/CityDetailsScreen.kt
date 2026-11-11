package com.razegon.moroccoapp.ui.screens.citydetails

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.viewmodel.CityDetailsAndroidViewModel
import com.razegon.moroccoapp.viewmodel.CityDetailsViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.ui.screens.citydetails.CityDetailsContent

@Composable
fun CityDetailsScreen(
    placeId: Int,
    onPlaceClick: (PlaceData) -> Unit,
    onOpenCityMap: (PlaceData, List<PlaceData>) -> Unit
) {
    val app = LocalContext.current.applicationContext as MyApp

    val factory = CityDetailsViewModelFactory(
        placeId = placeId,
        appContainer = app.appContainer
    )

    val viewModel: CityDetailsAndroidViewModel = viewModel(
        factory = factory
    )

    val wikimediaRepository = app.appContainer.wikimediaRepository
    val wikimediaFactory = WikimediaViewModelFactory(wikimediaRepository)

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaCityDetails",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Fullscreen state belongs to the screen, so it survives content recompositions.
    var isMapFullscreen by rememberSaveable { mutableStateOf(false) }

    // Back closes the fullscreen map before leaving the city details screen.
    BackHandler(enabled = isMapFullscreen) {
        isMapFullscreen = false
    }

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

    uiState.placeData?.let { placeData ->
        CityDetailsContent(
            placeData = placeData,
            children = uiState.children,
            wikidataInfo = uiState.wikidataInfo,
            wikimediaImages = wikimediaImages,
            imageLoader = app.wikimediaImageLoader,
            onPlaceClick = onPlaceClick,
            isMapFullscreen = isMapFullscreen,
            onOpenFullscreen = {
                onOpenCityMap(placeData, uiState.children)
                               },
            onCloseFullscreen = {
                isMapFullscreen = false
            }
        )
    }
}