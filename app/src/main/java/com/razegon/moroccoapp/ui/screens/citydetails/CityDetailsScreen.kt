package com.razegon.moroccoapp.ui.screens.citydetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    onPlaceClick: (PlaceData) -> Unit
) {
    // Access application-wide dependencies through MyApp,
    // including the shared AppContainer and platform-specific image loader.
    val app = LocalContext.current.applicationContext as MyApp

    // Create the ViewModel factory with the selected place ID
    // and the shared application dependencies.
    val factory = CityDetailsViewModelFactory(
        placeId = placeId,
        appContainer = app.appContainer
    )

    val viewModel: CityDetailsAndroidViewModel = viewModel(
        factory = factory
    )

    // Repository responsible for loading Wikimedia image metadata.
    val wikimediaRepository = app.appContainer.wikimediaRepository

    // Factory creates the Wikimedia ViewModel using the repository above.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaCityDetails",
        factory = wikimediaFactory
    )

    // Contains all Wikimedia metadata already loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the state of the selected city and its child places.
    val uiState by viewModel.uiState.collectAsState()

    // Request Wikimedia metadata whenever the current city
    // or its child places change.
    LaunchedEffect(uiState.placeData, uiState.children) {

        // Load all Wikimedia images assigned to the city itself.
        uiState.placeData?.place?.wikimediaImages?.forEach { imageRef ->

            imageRef?.let {
                wikimediaViewModel.getImage(it.pageId)
            }
        }

        // Load the first Wikimedia image for each child place.
        uiState.children.forEach { child ->

            child.place.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                    wikimediaViewModel.getImage(imageRef.pageId)
                }
        }
    }

    // Render the screen only when the selected place is available.
    uiState.placeData?.let { placeData ->

        CityDetailsContent(
            placeData = placeData,
            children = uiState.children,
            wikidataInfo = uiState.wikidataInfo,
            wikimediaImages = wikimediaImages,
            imageLoader = app.wikimediaImageLoader,
            onPlaceClick = onPlaceClick
        )
    }
}