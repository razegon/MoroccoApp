package com.razegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizStartScreen(
    onStartQuizClick: () -> Unit,
    onStartArcadeClick: () -> Unit,
    onRankingClick: () -> Unit,
    onArcadeRankingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QUIZ MAROKO",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Sprawdź swoją wiedzę o Maroku.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "10 PYTAŃ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "5 poziomów trudności",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Button(
            onClick = onStartQuizClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ROZPOCZNIJ QUIZ")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onStartArcadeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ARCADE MODE")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "RANKINGI TOP 20",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            onClick = onRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RANKING QUIZU")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            onClick = onArcadeRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RANKING ARCADE")
        }

    }
}