
package com.razegon.shared.ui.screens.home

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
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.cities_button
import com.razegon.shared.generated.resources.morocco_header
import com.razegon.shared.generated.resources.places_button
import com.razegon.shared.generated.resources.quiz_button
import com.razegon.shared.generated.resources.started_description
import com.razegon.shared.generated.resources.welcome_title
import com.razegon.shared.ui.model.HeroUi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCitiesClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    val heroUi = HeroUi(
        title = stringResource(Res.string.welcome_title),
        description = stringResource(Res.string.started_description),
        citiesButtonText = stringResource(Res.string.cities_button),
        placesButtonText = stringResource(Res.string.places_button),
        quizButtonText = stringResource(Res.string.quiz_button)
    )

    HeroSection(
        modifier = modifier,
        heroUi = heroUi,
        onCitiesBtnClick = onCitiesClick,
        onPlacesBtnClick = onPlacesClick,
        onQuizBtnClick = onQuizClick
    )
}

@Composable
private fun HeroSection(
    modifier: Modifier = Modifier,
    heroUi: HeroUi,
    onCitiesBtnClick: () -> Unit,
    onPlacesBtnClick: () -> Unit,
    onQuizBtnClick: () -> Unit
) {
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
                text = heroUi.title,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = heroUi.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.widthIn(max = 500.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onCitiesBtnClick) {
                    Text(text = heroUi.citiesButtonText)
                }

                Button(onClick = onPlacesBtnClick) {
                    Text(text = heroUi.placesButtonText)
                }

                Button(onClick = onQuizBtnClick) {
                    Text(text = heroUi.quizButtonText)
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
