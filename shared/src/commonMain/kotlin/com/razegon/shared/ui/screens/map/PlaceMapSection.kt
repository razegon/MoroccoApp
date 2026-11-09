package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razegon.shared.domain.config.MapConfig
import com.razegon.shared.domain.config.MapConfig.GIBRALTAR_STRAIT_ID
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraUpdate
import org.maplibre.compose.interaction.ClickResult
import org.maplibre.compose.interaction.MapInteractions
import org.maplibre.compose.layers.LocationIndicatorLayer
import org.maplibre.compose.location.LocationState
import org.maplibre.compose.map.CameraConstraints
import org.maplibre.compose.map.LocalMapState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.overlay.include
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position
import org.maplibre.spatialk.units.International.Meters
import com.razegon.shared.ui.components.FullscreenIcon


private const val MAP_STYLE_URL =
    "https://tiles.openfreemap.org/styles/bright"

@Composable
fun PlaceMapSection(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    modifier: Modifier = Modifier,
    onExpandClick: () -> Unit = {},
    isFullscreen: Boolean = false,
    placeId: Int,
    locationState: LocationState? = null
) {

    val mapArea = MapConfig.areaAround(
        latitude = latitude,
        longitude = longitude,
        minZoom = if (placeId == GIBRALTAR_STRAIT_ID) 8.0 else 15.0
    )

    val clickedLocation = remember {
        mutableStateOf<Position?>(null)
    }

    val isTracking = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(MAP_STYLE_URL),
        initialCameraPosition = CameraPosition(
            center = Position(
                latitude = latitude,
                longitude = longitude
            ),
            zoom = zoom.toDouble()
        )
    ) {
        checkNotNull(LocalMapState.current)

        if (isFullscreen) {
            locationState?.let { state ->
                val accuracyMeters = state.lastLocation
                    ?.horizontalAccuracy
                    ?.toDouble(Meters)

                val accuracyColor = getGpsAccuracyColor(accuracyMeters)

                LocationIndicatorLayer(
                    id = "user-location",
                    locationState = state,
                    accuracyRadiusColor = accuracyColor.copy(alpha = 0.15f),
                    accuracyRadiusBorderColor = accuracyColor,
                    hitPadding = 30.dp,
                    onClick = {
                        clickedLocation.value = state.lastLocation?.position
                        ClickResult.Consume
                    }
                )
            }
        }
    }

    LaunchedEffect(
        isTracking.value,
        locationState?.lastLocation?.position
    ) {
        val currentPosition = locationState?.lastLocation?.position

        if (isTracking.value && currentPosition != null) {
            mapState.animateCamera(
                update = CameraUpdate(
                    center = currentPosition
                )
            )
        }
    }

    LaunchedEffect(clickedLocation.value) {
        val targetLocation = clickedLocation.value

        if (targetLocation != null) {
            mapState.animateCamera(
                update = CameraUpdate(
                    center = targetLocation
                )
            )

            clickedLocation.value = null
        }
    }

    val cameraConstraints = CameraConstraints {
        boundingBox = BoundingBox(
            west = mapArea.west,
            south = mapArea.south,
            east = mapArea.east,
            north = mapArea.north
        )
        minZoom = mapArea.minZoom
        maxZoom = mapArea.maxZoom

    } ?: CameraConstraints.Standard

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        MaplibreMap(
            state = mapState,
            modifier = Modifier.fillMaxSize(),
            cameraConstraints = cameraConstraints,
            interactions = MapInteractions {
                camera {
                    pan {
                        onStart {
                            isTracking.value = false
                        }
                    }
                }
            }
        ) {
            include(MapOverlay.None)

            if (isFullscreen && locationState != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    locationState.lastLocation?.horizontalAccuracy?.let { accuracy ->
                        LocationAccuracyIndicator(
                            horizontalAccuracy = accuracy.toDouble(Meters)
                        )
                    }

                    MapTrackingButton(
                        isTracking = isTracking.value,
                        onClick = {
                            val currentPosition = locationState.lastLocation?.position

                            if (currentPosition != null) {
                                isTracking.value = true

                                coroutineScope.launch {
                                    mapState.animateCamera(
                                        update = CameraUpdate(
                                            center = currentPosition
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }

            Surface(
                onClick = onExpandClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 24.dp, end = 8.dp)
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
