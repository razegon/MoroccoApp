package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.repository.QuizRepository
import com.rezegon.shared.presentation.viewmodel.QuizViewModel as SharedQuizViewModel

class QuizAndroidViewModel(
    repository: QuizRepository
) : ViewModel() {

    private val sharedViewModel = SharedQuizViewModel(
        repository = repository,
        scope = viewModelScope
    )
    val currentQuestionIndex = sharedViewModel.currentQuestionIndex
    val selectedAnswerIndex = sharedViewModel.selectedAnswerIndex
    val score = sharedViewModel.score
    val selectedDifficulty = sharedViewModel.selectedDifficulty
    val isAnswerChecked = sharedViewModel.isAnswerChecked
    val currentQuestion get() = sharedViewModel.currentQuestion
    val questionCount get() = sharedViewModel.questionCount
    val isResultSaved = sharedViewModel.isResultSaved
    val isLoading = sharedViewModel.isLoading
    val error = sharedViewModel.error

    fun selectAnswer(answerIndex: Int) {
        sharedViewModel.selectAnswer(answerIndex)
    }

    fun nextQuestion(): Boolean {
        return sharedViewModel.nextQuestion()
    }

    fun startQuiz(difficulty: com.rezegon.shared.domain.model.QuizDifficulty) {
        sharedViewModel.startQuiz(difficulty)
    }

    fun restartQuiz() {
        sharedViewModel.restartQuiz()
    }

    fun markResultAsSaved() {
        sharedViewModel.markResultAsSaved()
    }
}
