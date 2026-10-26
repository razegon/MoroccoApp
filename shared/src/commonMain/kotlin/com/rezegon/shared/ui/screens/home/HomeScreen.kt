
package com.rezegon.shared.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rezegon.shared.generated.resources.Res
import com.rezegon.shared.generated.resources.cities_button
import com.rezegon.shared.generated.resources.morocco_header
import com.rezegon.shared.generated.resources.places_button
import com.rezegon.shared.generated.resources.quiz_button
import com.rezegon.shared.generated.resources.started_description
import com.rezegon.shared.generated.resources.welcome_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCitiesClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    val title = stringResource(Res.string.welcome_title)
    val description = stringResource(Res.string.started_description)
    val citiesButtonText = stringResource(Res.string.cities_button)
    val placesButtonText = stringResource(Res.string.places_button)
    val quizButtonText = stringResource(Res.string.quiz_button)

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.morocco_header),
            contentDescription = "Morocco",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        GradientOverlay()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.widthIn(max = 500.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onCitiesClick) {
                    Text(text = citiesButtonText)
                }

                Button(onClick = onPlacesClick) {
                    Text(text = placesButtonText)
                }

                Button(onClick = onQuizClick) {
                    Text(text = quizButtonText)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun GradientOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.70f),
                        Color.Black.copy(alpha = 0.30f),
                        Color.Transparent
                    )
                )
            )
    )
}

