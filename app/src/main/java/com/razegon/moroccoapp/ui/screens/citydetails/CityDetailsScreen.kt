package com.razegon.moroccoapp.ui.screens.citydetails

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.ui.components.DetailsPager
import com.razegon.moroccoapp.ui.components.PlaceListItem
import com.razegon.moroccoapp.ui.components.WikidataInfoSection
import com.razegon.moroccoapp.ui.components.createPages
import com.razegon.moroccoapp.viewmodel.CityDetailsAndroidViewModel
import com.razegon.moroccoapp.viewmodel.CityDetailsViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData

@Composable
fun CityDetailsScreen(
    placeId: Int,
    onPlaceClick: (PlaceData) -> Unit
) {
    // MyApp contains application-wide dependencies,
    // including the shared Wikimedia memory cache and Room database.
    val app = LocalContext.current.applicationContext as MyApp

    // Factory creates the ViewModel and passes the selected place ID
    // together with the application instance.
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

        // Build the pages displayed by DetailsPager.
        // Wikimedia URLs are taken from the loaded metadata map,
        // while local images can be used as a fallback.
        val pages = createPages(
            wikimediaImages = wikimediaImages,
            images = placeData.place.placeImages,
            descriptions = placeData.content.descriptions,
            wikimediaRefs = placeData.place.wikimediaImages
        )

        Log.d(
            "WIKIMEDIA_UI",
            "children=${uiState.children.size}, images=${wikimediaImages.keys}"
        )

        DetailsPager(
            placeData = placeData,
            pages = pages,
            wikimediaImages = wikimediaImages
        ) {

            // Display additional Wikidata information
            // when it has been successfully loaded.
            uiState.wikidataInfo?.let { info ->
                WikidataInfoSection(
                    info = info
                )
            }

            // Display the places belonging to the current city.
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(uiState.children) { child ->

                    // Prefer the Wikimedia image when its metadata
                    // has already been loaded. Fall back to the local image.
                    val imageUrl =
                        child.place.wikimediaImages
                            .firstOrNull()
                            ?.let { wikimediaImages[it.pageId]?.url }
                            ?: child.place.placeImages.firstOrNull()

                    Log.d(
                        "WIKIMEDIA_ROW",
                        "child=${child.place.placeId}, " +
                                "imageRef=${child.place.wikimediaImages.firstOrNull()?.pageId}, " +
                                "url=$imageUrl"
                    )

                    PlaceListItem(
                        placeData = child,
                        imageUrl = imageUrl,
                        modifier = Modifier.width(180.dp),
                        imageHeight = 140.dp,
                        onClick = onPlaceClick
                    )
                }
            }
        }
    }
}