package com.rezegon.moroccoapp.ui.screens.city

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.repository.WikimediaRepositoryImpl
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.ui.components.CityListItem
import com.rezegon.moroccoapp.viewmodel.CitiesViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModelFactory

@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (Place) -> Unit
) {
    val viewModel: CitiesViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    // Access the shared application instance.
    // MyApp contains application-wide dependencies such as
    // the memory cache and the Room database.
    val app = LocalContext.current.applicationContext as MyApp

    // Repository responsible for loading Wikimedia image metadata.
    //
    // It uses:
    // - Wikimedia API as the remote data source,
    // - shared memory cache for fast access,
    // - Room DAO for persistent local storage.
    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = app.wikimediaCache,
        dao = app.database.wikimediaDao()
    )

    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
        key = "wikimediaCities",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Request image metadata for the first Wikimedia image
    // associated with each city.
    LaunchedEffect(uiState.places) {
        uiState.places.forEach { place ->
            place.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                    wikimediaViewModel.getImage(imageRef.pageId)
                }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = stringResource(R.string.cities_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 16.dp,
                end = 20.dp,
                bottom = 8.dp
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {

            items(uiState.places) { place ->

                // Prefer the Wikimedia image when it is available.
                // Fall back to the locally defined image otherwise.
                val imageUrl =
                    place.wikimediaImages
                        .firstOrNull()
                        ?.let { wikimediaImages[it.pageId]?.url }
                        ?: place.placeImages.firstOrNull()

                CityListItem(
                    place = place,
                    imageUrl = imageUrl,
                    onClick = onPlaceClick
                )
            }
        }
    }
}