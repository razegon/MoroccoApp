package com.razegon.moroccoapp.ui.screens.places

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.R
import com.razegon.moroccoapp.viewmodel.PlacesAndroidViewModel
import com.razegon.moroccoapp.viewmodel.PlacesViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.ui.screens.places.PlacesContent

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (PlaceData) -> Unit
) {
    // Access the custom application instance.
    // MyApp contains application-wide dependencies such as
    // the shared Wikimedia memory cache and Room database.
    val application = LocalContext.current.applicationContext as MyApp

    // ViewModel responsible for loading the list of places.
    val viewModel: PlacesAndroidViewModel = viewModel(
        factory = PlacesViewModelFactory(application.appContainer)
    )

    // Repository responsible for loading Wikimedia image metadata.
    val wikimediaRepository = application.appContainer.wikimediaRepository

    // Factory creates the Wikimedia ViewModel using the repository.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaPlaces",
        factory = wikimediaFactory
    )

    // Contains Wikimedia metadata currently loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the current state of the Places screen.
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.places) {
        uiState.places.forEach { placeData ->
            placeData.place.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                    wikimediaViewModel.getImage(imageRef.pageId)
                }
        }
    }

    PlacesContent(
        places = uiState.places,
        wikimediaImages = wikimediaImages,
        imageLoader = application.wikimediaImageLoader,
        title = stringResource(R.string.places_title),
        onPlaceClick = onPlaceClick,
        modifier = modifier.statusBarsPadding()
    )
}