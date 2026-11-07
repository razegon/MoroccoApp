package com.razegon.shared.ui.screens.placedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.overlay.include
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

private const val MAP_STYLE_URL =
    "https://tiles.openfreemap.org/styles/bright"

@Composable
fun PlaceMapSection(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    modifier: Modifier = Modifier,
    onExpandClick: () -> Unit = {},
    isFullscreen: Boolean = false
) {
    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(MAP_STYLE_URL),
        initialCameraPosition = CameraPosition(
            center = Position(
                latitude = latitude,
                longitude = longitude
            ),
            zoom = zoom.toDouble()
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        MaplibreMap(
            state = mapState,
            modifier = Modifier.fillMaxSize()
        ) {
            include(MapOverlay.None)

            Surface(
                onClick = onExpandClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 24.dp,
                        end = 8.dp
                    )
                    .size(32.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FullscreenIcon(isFullscreen = isFullscreen)
                }
            }
        }
    }
}

@Composable
private fun FullscreenIcon(
    isFullscreen: Boolean
) {
    Icon(
        imageVector = if (isFullscreen) {
            Icons.Default.CloseFullscreen
        } else {
            Icons.Default.OpenInFull
        },
        contentDescription = if (isFullscreen) {
            "Zamknij pełny ekran"
        } else {
            "Otwórz pełny ekran"
        },
        tint = Color.DarkGray,
        modifier = Modifier.size(24.dp)
    )
}

@Preview
@Composable
private fun PlaceMapSectionPreview() {
    PlaceMapSection(
        latitude = 34.07196,
        longitude = -5.85721,
        zoom = 16f,
        modifier = Modifier
            .fillMaxSize()
    )
}