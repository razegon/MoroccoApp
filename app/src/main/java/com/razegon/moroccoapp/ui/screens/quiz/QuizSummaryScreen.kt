package com.razegon.moroccoapp.ui.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.razegon.moroccoapp.viewmodel.QuizAndroidViewModel
import com.razegon.moroccoapp.viewmodel.QuizResultAndroidViewModel
import com.razegon.shared.domain.model.QuizResult
import com.razegon.shared.ui.screens.quiz.QuizSummaryScreen as SharedQuizSummaryScreen

@Composable
fun QuizSummaryScreen(
    viewModel: QuizAndroidViewModel,
    resultViewModel: QuizResultAndroidViewModel,
    onRestartClick: () -> Unit,
    onRankingClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the final quiz score.
    val score by viewModel.score.collectAsState()

    val isResultSaved by viewModel.isResultSaved.collectAsState()

    SharedQuizSummaryScreen(
        score = score,
        questionCount = viewModel.questionCount,
        isResultSaved = isResultSaved,
        onSaveResult = { nickname ->
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
        onRankingClick = onRankingClick,
        onRestartClick = onRestartClick,
        onHomeClick = onHomeClick,
        modifier = modifier
    )
}