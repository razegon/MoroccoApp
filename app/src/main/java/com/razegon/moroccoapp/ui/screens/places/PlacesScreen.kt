package com.razegon.moroccoapp.ui.screens.places

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.places_title
import org.jetbrains.compose.resources.stringResource
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
    // Access application-wide dependencies through MyApp,
    // including the shared AppContainer and platform-specific image loader.
    val application = LocalContext.current.applicationContext as MyApp

    // Android ViewModel providing access to the shared places state.
    val viewModel: PlacesAndroidViewModel = viewModel(
        factory = PlacesViewModelFactory(application.appContainer)
    )

    // Repository responsible for loading Wikimedia image metadata.
    val wikimediaRepository = application.appContainer.wikimediaRepository

    // Factory creates the Wikimedia ViewModel using the repository.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    // Create the Android Wikimedia ViewModel using the shared repository.
    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaPlaces",
        factory = wikimediaFactory
    )

    // Contains Wikimedia metadata currently loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the current state of the Places screen.
    val uiState by viewModel.uiState.collectAsState()

    // Load metadata for the first Wikimedia image associated with each place.
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
        title = stringResource(Res.string.places_title),
        onPlaceClick = onPlaceClick,
        modifier = modifier.statusBarsPadding()
    )
}