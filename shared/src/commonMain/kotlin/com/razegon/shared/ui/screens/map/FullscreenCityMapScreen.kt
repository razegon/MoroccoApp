package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.razegon.shared.presentation.model.CityMapUiState

@Composable
fun FullscreenCityMapScreen(
    uiState: CityMapUiState,
    onPlaceClick: (com.razegon.shared.domain.model.PlaceData) -> Unit,
    onBackClick: () -> Unit
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
                isFullscreen = true
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
