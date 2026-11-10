package com.razegon.shared.map.marker

import org.maplibre.compose.expressions.ast.Expression
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.interpolate
import org.maplibre.compose.expressions.dsl.linear
import org.maplibre.compose.expressions.dsl.zoom
import org.maplibre.compose.expressions.value.FloatValue

object MarkerScale {

    fun iconSize(): Expression<FloatValue> =
        interpolate(
            linear(),
            zoom(),
            8 to const(0.65f),
            10 to const(0.75f),
            12 to const(0.9f),
            14 to const(1.1f),
            16 to const(1.35f),
            18 to const(1.6f)
        )
}