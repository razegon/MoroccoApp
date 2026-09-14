package com.rezegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.domain.model.QuizResult
import com.rezegon.moroccoapp.viewmodel.QuizResultViewModel
import com.rezegon.moroccoapp.viewmodel.QuizViewModel

@Composable
fun QuizSummaryScreen(
    viewModel: QuizViewModel,
    resultViewModel: QuizResultViewModel,
    onRestartClick: () -> Unit,
    onRankingClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the final quiz score.
    val score by viewModel.score.collectAsState()

    val isResultSaved by viewModel.isResultSaved.collectAsState()

    // Nickname entered by the player.
    var nickname by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = nickname,
            onValueChange = { newValue ->
                if (newValue.length <= 15) {
                    nickname = newValue
                }
            },
            enabled = !isResultSaved,
            label = {
                Text("Twój nick")
            },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                val result = QuizResult(
                    nickname = nickname.trim(),
                    difficulty = viewModel.selectedDifficulty.value!!,
                    score = score,
                    totalQuestions = viewModel.questionCount,
                    date = System.currentTimeMillis()
                )

                resultViewModel.saveResult(result)
                viewModel.markResultAsSaved()
            },
            enabled = nickname.trim().isNotEmpty() && !isResultSaved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isResultSaved) {
                    "WYNIK ZAPISANY"
                } else "ZAPISZ WYNIK"
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("TOP 10")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ZAGRAJ PONOWNIE")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("POWRÓT DO MENU")
        }
    }
}