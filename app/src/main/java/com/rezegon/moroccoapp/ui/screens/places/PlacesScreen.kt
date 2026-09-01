package com.rezegon.moroccoapp.ui.screens.places

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
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.ui.components.PlaceListItem
import com.rezegon.moroccoapp.viewmodel.PlacesViewModel
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.cache.WikimediaMemoryCache
import com.rezegon.moroccoapp.data.repository.WikimediaRepositoryImpl
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModelFactory

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (Place) -> Unit
) {
    val viewModel: PlacesViewModel = viewModel()

    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = WikimediaMemoryCache()
    )

    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
        key = "wikimediaPlaces",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = stringResource(R.string.places_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 16.dp,
                end = 20.dp,
                bottom = 8.dp
            )
        )

        LaunchedEffect(uiState.places) {
            uiState.places.forEach { place ->
                place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaViewModel.getImage(imageRef.pageId)
                    }
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.places) { place ->

                val imageUrl = place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaImages[imageRef.pageId]?.url
                    } ?: place.placeImages.firstOrNull()

                PlaceListItem(
                    place = place,
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPlaceClick
                )
            }
        }
    }
}

