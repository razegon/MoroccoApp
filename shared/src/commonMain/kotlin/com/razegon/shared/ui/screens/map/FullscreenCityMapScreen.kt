package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.razegon.shared.presentation.model.CityMapUiState
import coil3.ImageLoader
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.model.PlaceData

@Composable
fun FullscreenCityMapScreen(
    uiState: CityMapUiState,
    onPlaceClick: (PlaceData) -> Unit,
    onBackClick: () -> Unit,
    selectedPlaceId: Int?,
    onPlaceSelected: (Int?) -> Unit,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val placeData = uiState.placeData

        if (placeData != null) {
            CityMapSection(
                placeData = placeData,
                children = uiState.children,
                modifier = Modifier.fillMaxSize(),
                onPlaceClick = onPlaceClick,
                onExpandClick = onBackClick,
                isFullscreen = true,
                selectedPlaceId = selectedPlaceId,
                onPlaceSelected = onPlaceSelected,
                wikimediaImages = wikimediaImages,
                imageLoader = imageLoader
            )
        } else if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = uiState.error ?: "Nie udało się wczytać mapy miasta.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
