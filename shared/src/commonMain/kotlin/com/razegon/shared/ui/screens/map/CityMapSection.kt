package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.ui.components.FullscreenIcon
import kotlinx.serialization.json.jsonPrimitive
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.interaction.ClickResult
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.overlay.include
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

private const val CITY_MAP_STYLE_URL =
    "https://tiles.openfreemap.org/styles/bright"

/**
 * Displays child places on a city map.
 *
 * This component is responsible only for browsing city attractions.
 * It does not request location permissions or track the user's position.
 */
@Composable
fun CityMapSection(
    placeData: PlaceData,
    children: List<PlaceData>,
    modifier: Modifier = Modifier,
    onPlaceClick: (PlaceData) -> Unit,
    onExpandClick: () -> Unit = {},
    isFullscreen: Boolean = false,
) {
    // Ignore places without valid coordinates.
    val mappedChildren = children.filter { child ->
        val latitude = child.place.latitude
        val longitude = child.place.longitude

        latitude != null &&
                longitude != null &&
                latitude.isFinite() &&
                longitude.isFinite() &&
                latitude in -90.0..90.0 &&
                longitude in -180.0..180.0
    }

    // Prefer the city's own coordinates; otherwise use the center of its children.
    val centerLatitude = placeData.place.latitude
        ?: mappedChildren.mapNotNull { it.place.latitude }
            .takeIf { it.isNotEmpty() }
            ?.average()

    val centerLongitude = placeData.place.longitude
        ?: mappedChildren.mapNotNull { it.place.longitude }
            .takeIf { it.isNotEmpty() }
            ?.average()

    if (centerLatitude == null || centerLongitude == null) {
        return
    }

    // GeoJSON uses [longitude, latitude] coordinate order.
    val geoJson = buildString {
        append("""{"type":"FeatureCollection","features":[""")

        append(
            mappedChildren.joinToString(",") { child ->
                """{"type":"Feature","properties":{"placeId":${child.place.placeId}},"geometry":{"type":"Point","coordinates":[${child.place.longitude},${child.place.latitude}]}}"""
            }
        )

        append("]}")
    }

    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(CITY_MAP_STYLE_URL),
        initialCameraPosition = CameraPosition(
            center = Position(
                latitude = centerLatitude,
                longitude = centerLongitude
            ),
            zoom = (placeData.place.zoom ?: 13f).toDouble()
        )
    ) {
        val placesSource = rememberGeoJsonSource(
            data = GeoJsonData.JsonString(geoJson)
        )

        CircleLayer(
            id = "city-child-place-markers",
            source = placesSource,
            color = const(Color(0xFF1976D2)),
            radius = const(4.dp),
            strokeColor = const(Color.White),
            strokeWidth = const(2.dp),
            hitPadding = 12.dp,
            onClick = { features ->
                val placeId = features
                    .firstOrNull()
                    ?.properties
                    ?.get("placeId")
                    ?.jsonPrimitive
                    ?.content
                    ?.toIntOrNull()

                val selectedPlace = mappedChildren.firstOrNull {
                    it.place.placeId == placeId
                }

                selectedPlace?.let(onPlaceClick)

                if (selectedPlace != null) {
                    ClickResult.Consume
                } else {
                    ClickResult.Pass
                }
            }
        )
    }

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

