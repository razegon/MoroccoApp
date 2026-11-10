package com.razegon.shared.ui.components.map

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FullscreenIcon(
    isFullscreen: Boolean,
    modifier: Modifier = Modifier
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