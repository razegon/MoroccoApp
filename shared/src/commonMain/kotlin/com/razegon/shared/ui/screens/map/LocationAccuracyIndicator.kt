
package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razegon.shared.ui.theme.GpsAccuracyGood
import com.razegon.shared.ui.theme.GpsAccuracyMedium
import com.razegon.shared.ui.theme.GpsAccuracyPoor
import com.razegon.shared.ui.theme.GpsAccuracyUnknown
import kotlin.math.roundToInt


// Wspólna klasyfikacja dokładności GPS
fun getGpsAccuracyColor(horizontalAccuracy: Double?): Color {
    val validAccuracy = horizontalAccuracy?.takeIf {
        it.isFinite() && it >= 0.0
    }

    return when {
        validAccuracy == null -> GpsAccuracyUnknown
        validAccuracy <= 5.0 -> GpsAccuracyGood
        validAccuracy <= 20.0 -> GpsAccuracyMedium
        else -> GpsAccuracyPoor
    }
}

@Composable
fun LocationAccuracyIndicator(
    horizontalAccuracy: Double?,
    modifier: Modifier = Modifier
) {
    val validAccuracy = horizontalAccuracy?.takeIf {
        it.isFinite() && it >= 0.0
    }

    val accuracyColor = getGpsAccuracyColor(horizontalAccuracy)

    val label = validAccuracy?.let {
        "GPS ±${it.roundToInt()} m"
    } ?: "GPS: nieznana dokładność"

    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.0.dp, accuracyColor.copy(alpha = 0.65f)),
        shadowElevation = 0.dp
    ) {
        Text(
            text = label,
            color = accuracyColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(
                horizontal = 7.dp,
                vertical = 3.dp
            )
        )
    }
}
