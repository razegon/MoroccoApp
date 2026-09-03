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
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rezegon.moroccoapp.data.repository.QuizRepositoryImpl
import com.rezegon.moroccoapp.viewmodel.QuizViewModel
import com.rezegon.moroccoapp.viewmodel.QuizViewModelFactory

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    onSummaryClick: () -> Unit
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Quiz",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${currentQuestionIndex + 1} / ${viewModel.questionCount}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        question.answers.forEachIndexed { index, answer ->

            val isSelected = selectedAnswerIndex == index
            val isCorrect = index == question.correctAnswerIndex

            val buttonColor =
                when {
                    !isAnswerChecked -> {
                        ButtonDefaults.buttonColors()
                    }

                    isCorrect -> {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Green
                        )
                    }

                    isSelected -> {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    }

                    else -> {
                        ButtonDefaults.buttonColors()
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val movedToNextQuestion = viewModel.nextQuestion()

                if (!movedToNextQuestion) onSummaryClick()
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Wynik: $score",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

