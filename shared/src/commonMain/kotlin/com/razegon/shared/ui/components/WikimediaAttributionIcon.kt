package com.razegon.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.ic_info
import org.jetbrains.compose.resources.painterResource

@Composable
fun WikimediaAttributionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Provide a clickable information icon for Wikimedia image attribution.
    IconButton(
        onClick = onClick,
        modifier = modifier.size(36.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_info),
            contentDescription = "Informacje o zdjęciu",
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = CircleShape
                )
                .padding(6.dp)
        )
    }
}