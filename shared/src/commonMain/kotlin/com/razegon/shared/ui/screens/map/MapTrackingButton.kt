package com.razegon.shared.ui.screens.map

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razegon.shared.ui.theme.MaterialGreen700

@Composable
fun MapTrackingButton(
    isTracking: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackingColor = MaterialGreen700
    val inactiveColor = Color.DarkGray
    val inactiveColorLight = Color.LightGray

    val iconColor by animateColorAsState(
        targetValue = if (isTracking) trackingColor else inactiveColor,
        label = "GPS icon color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isTracking) trackingColor else inactiveColorLight,
        label = "GPS border color"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(32.dp),
        shape = CircleShape,
        border = BorderStroke(
            width = if (isTracking) 2.dp else 1.dp,
            color = borderColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = iconColor
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MyLocation,
            contentDescription = if (isTracking) {
                "Śledzenie GPS aktywne"
            } else {
                "Wznów śledzenie GPS"
            },
            modifier = Modifier.size(22.dp)
        )
    }
}
