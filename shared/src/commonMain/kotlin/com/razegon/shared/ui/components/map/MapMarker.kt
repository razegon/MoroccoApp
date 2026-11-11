package com.razegon.shared.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.beach
import com.razegon.shared.generated.resources.city
import com.razegon.shared.generated.resources.hotel
import com.razegon.shared.generated.resources.market
import com.razegon.shared.generated.resources.monument
import com.razegon.shared.generated.resources.museum
import com.razegon.shared.generated.resources.nature
import com.razegon.shared.generated.resources.park
import com.razegon.shared.generated.resources.religious_site
import com.razegon.shared.generated.resources.restaurant
import com.razegon.shared.generated.resources.shop
import com.razegon.shared.generated.resources.viewpoint
import com.razegon.shared.map.marker.MarkerScale
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.painterResource
import org.maplibre.compose.expressions.dsl.Feature.has
import org.maplibre.compose.expressions.dsl.case
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.convertToString
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.interpolate
import org.maplibre.compose.expressions.dsl.linear
import org.maplibre.compose.expressions.dsl.not
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.interaction.ClickResult
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonSource
import org.maplibre.compose.util.MaplibreComposable

@Composable
@MaplibreComposable
fun MapMarker(
    source: GeoJsonSource,
    places: List<PlaceData>,
    onPlaceClick: (PlaceData) -> Unit
) {

    CircleLayer(
        id = "city-place-clusters",
        source = source,
        filter = has("point_count"),

        radius = interpolate(
            linear(),
            feature["point_count"].cast(),
            2 to const(20.dp),
            5 to const(23.dp),
            10 to const(27.dp),
            20 to const(32.dp),
            50 to const(38.dp)
        ),

        color = const(Color(0xFF176B87)),
        opacity = const(0.5f),

        strokeWidth = const(1.dp),
        strokeColor = const(Color.DarkGray),
        strokeOpacity = const(0.9f)
    )

    SymbolLayer(
        id = "city-place-cluster-count",
        source = source,
        filter = has("point_count"),
        textField = feature["123"].convertToString().cast(),
        textFont = const(listOf("Open Sans Regular")),
        textColor = const(Color.White),
        textSize = const(13.sp),
        textAllowOverlap = const(true)
    )

    SymbolLayer(
        id = "city-child-place-markers",
        source = source,
        filter = !has("point_count"),
        iconImage = switch(
            input = feature["iconKey"],
            case(
                label = "city",
                output = image(painterResource(Res.drawable.city))
            ),
            case(
                label = "monument",
                output = image(painterResource(Res.drawable.monument))
            ),
            case(
                label = "religious_site",
                output = image(painterResource(Res.drawable.religious_site))
            ),
            case(
                label = "beach",
                output = image(painterResource(Res.drawable.beach))
            ),
            case(
                label = "museum",
                output = image(painterResource(Res.drawable.museum))
            ),
            case(
                label = "restaurant",
                output = image(painterResource(Res.drawable.restaurant))
            ),
            case(
                label = "hotel",
                output = image(painterResource(Res.drawable.hotel))
            ),
            case(
                label = "park",
                output = image(painterResource(Res.drawable.park))
            ),
            case(
                label = "nature",
                output = image(painterResource(Res.drawable.nature))
            ),
            case(
                label = "viewpoint",
                output = image(painterResource(Res.drawable.viewpoint))
            ),
            case(
                label = "shop",
                output = image(painterResource(Res.drawable.shop))
            ),
            case(
                label = "market",
                output = image(painterResource(Res.drawable.market))
            ),
            fallback = image(painterResource(Res.drawable.monument))
        ),
        iconSize = MarkerScale.iconSize(),
        iconAllowOverlap = const(true),
        hitPadding = 12.dp,
        onClick = { features ->
            val placeId = features
                .firstOrNull()
                ?.properties
                ?.get("placeId")
                ?.jsonPrimitive
                ?.content
                ?.toIntOrNull()

            val selectedPlace = places.firstOrNull {
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