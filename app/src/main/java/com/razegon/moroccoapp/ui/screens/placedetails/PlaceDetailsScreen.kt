package com.razegon.moroccoapp.ui.screens.placedetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.viewmodel.PlaceDetailsAndroidViewModel
import com.razegon.moroccoapp.viewmodel.PlaceDetailsViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.ui.screens.placedetails.PlaceDetailsContent

@Composable
fun PlaceDetailsScreen(
    placeId: Int,
    onOpenMap: (Double, Double, Float) -> Unit
) {
    // Access application-wide dependencies through MyApp,
    // including the shared AppContainer and platform-specific image loader.
    val application = LocalContext.current.applicationContext as MyApp

    // Create the ViewModel factory with the selected place ID
    // and the shared application dependencies.
    val factory = PlaceDetailsViewModelFactory(
        placeId = placeId,
        appContainer = application.appContainer
    )

    // Create the Android Wikimedia ViewModel using the shared repository.
    val viewModel: PlaceDetailsAndroidViewModel = viewModel(
        factory = factory
    )

    // Repository responsible for loading Wikimedia image metadata.
    val wikimediaRepository = application.appContainer.wikimediaRepository

    // Factory creates the Wikimedia ViewModel using the repository.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaPlaceDetails",
        factory = wikimediaFactory
    )

    // Contains Wikimedia metadata currently loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the current state of the selected place.
    val uiState by viewModel.uiState.collectAsState()

    uiState.place?.let { placeData ->

        // Request Wikimedia metadata for all images
        // assigned to the current place.
        LaunchedEffect(placeData.place.wikimediaImages) {

            placeData.place.wikimediaImages.forEach { imageRef ->

                imageRef?.let {
                    wikimediaViewModel.getImage(it.pageId)
                }
            }
        }

        PlaceDetailsContent(
            placeData = placeData,
            wikimediaImages = wikimediaImages,
            imageLoader = application.wikimediaImageLoader,
            onOpenMap = onOpenMap
        )
    }
}
