package com.rezegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.viewmodel.QuizViewModel

@Composable
fun QuizSummaryScreen(
    viewModel: QuizViewModel,
    onRestartClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Observe the final quiz score.
    val score by viewModel.score.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "KONIEC QUIZU",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "$score / ${viewModel.questionCount}",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "ZAGRAJ PONOWNIE")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "POWRÓT DO MENU")
        }
    }
}

