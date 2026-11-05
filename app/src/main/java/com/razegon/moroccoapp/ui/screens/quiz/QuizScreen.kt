package com.razegon.moroccoapp.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.razegon.moroccoapp.viewmodel.QuizAndroidViewModel
import com.razegon.shared.ui.screens.quiz.QuizGameScreen

@Composable
fun QuizScreen(
    viewModel: QuizAndroidViewModel,
    modifier: Modifier = Modifier,
    onSummaryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()

    val currentQuestionIndex by
    viewModel.currentQuestionIndex.collectAsState()

    val selectedAnswerIndex by
    viewModel.selectedAnswerIndex.collectAsState()

    val score by
    viewModel.score.collectAsState()

    val isAnswerChecked by
    viewModel.isAnswerChecked.collectAsState()

    val question = questions.getOrNull(currentQuestionIndex) ?: return

    QuizGameScreen(
        currentQuestion = question,
        currentQuestionIndex = currentQuestionIndex,
        questionCount = questions.size,
        selectedAnswerIndex = selectedAnswerIndex,
        score = score,
        isAnswerChecked = isAnswerChecked,
        onAnswerSelect = viewModel::selectAnswer,
        onNextQuestion = {
            if (!viewModel.nextQuestion()) {
                onSummaryClick()
            }
        },
        onExitClick = onExitClick,
        modifier = modifier
    )
}