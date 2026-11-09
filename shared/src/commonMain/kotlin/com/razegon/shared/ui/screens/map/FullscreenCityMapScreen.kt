package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.razegon.shared.domain.model.PlaceData

@Composable
fun FullscreenCityMapScreen(
    placeData: PlaceData,
    children: List<PlaceData>,
    onPlaceClick: (PlaceData) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CityMapSection(
            placeData = placeData,
            children = children,
            modifier = Modifier.fillMaxSize(),
            onPlaceClick = onPlaceClick,
            onExpandClick = onBackClick,
            isFullscreen = true
        )
    }
}