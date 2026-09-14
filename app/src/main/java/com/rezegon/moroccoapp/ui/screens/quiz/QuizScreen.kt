package com.rezegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.viewmodel.QuizViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    onSummaryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    // Observe the current quiz state.
    val currentQuestionIndex by
    viewModel.currentQuestionIndex.collectAsState()

    val selectedAnswerIndex by
    viewModel.selectedAnswerIndex.collectAsState()

    val score by
    viewModel.score.collectAsState()

    val isAnswerChecked by
    viewModel.isAnswerChecked.collectAsState()

    val question = viewModel.currentQuestion

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                showExitDialog = true
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Wyjdź z quizu"
            )
        }

        Text(
            text = "Quiz",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "${currentQuestionIndex + 1} / ${viewModel.questionCount}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        question.answers.forEachIndexed { index, answer ->

            val isSelected = selectedAnswerIndex == index
            val isCorrect = index == question.correctAnswerIndex

            val buttonColor = when {
                !isAnswerChecked -> {
                    ButtonDefaults.buttonColors()
                }

                isCorrect -> {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF4CAF50),
                        disabledContentColor = Color.White
                    )
                }

                isSelected -> {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE53935),
                        disabledContentColor = Color.White
                    )
                }

                else -> {
                    ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.selectAnswer(index)
                },
                enabled = !isAnswerChecked,
                colors = buttonColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = answer)
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                val movedToNextQuestion = viewModel.nextQuestion()

                if (!movedToNextQuestion) {
                    onSummaryClick()
                }
            },
            enabled = isAnswerChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (
                    currentQuestionIndex == viewModel.questionCount - 1
                ) {
                    "PODSUMOWANIE"
                } else {
                    "NASTĘPNE"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Wynik: $score",
            style = MaterialTheme.typography.bodyLarge
        )
    }

    // Confirms that leaving the quiz is intentional before discarding progress.
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            title = {
                Text("Czy chcesz zakończyć Quiz")
            },
            text = {
                Text("Twój dotychczasowy wynik zostanie utracony.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onExitClick()
                    }
                ) {
                    Text("Wyjdź")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text("Zostań")
                }
            }
        )
    }
}