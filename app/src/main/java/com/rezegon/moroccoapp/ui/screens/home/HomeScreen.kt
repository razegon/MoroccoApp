package com.rezegon.moroccoapp.ui.screens.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.ui.model.HeroUi

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCitiesClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onQuizClick: () -> Unit
) {

    // Contains the texts and labels displayed in the hero section.
    val heroUi = HeroUi(
        title = stringResource(R.string.welcome_title),
        description = stringResource(R.string.started_description),
        citiesButtonText = stringResource(R.string.cities_button),
        placesButtonText = stringResource(R.string.places_button),
        quizButtonText = stringResource(R.string.quiz_button)
    )

    // Passes navigation callbacks to the corresponding buttons.
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.morocco_header),
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

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = heroUi.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.widthIn(max = 500.dp)
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = onCitiesBtnClick
                ) {
                    Text(
                        text = heroUi.citiesButtonText
                    )
                }

                Button(
                    onClick = onPlacesBtnClick
                ) {
                    Text(
                        text = heroUi.placesButtonText
                    )
                }

                // Opens the quiz when the user taps the quiz button.
                Button(
                    onClick = onQuizBtnClick
                ) {
                    Text(
                        text = heroUi.quizButtonText
                    )
                }

                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

@Composable
fun GradientOverlay() {
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

