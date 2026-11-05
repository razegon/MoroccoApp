package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.QuizRepository
import com.razegon.shared.presentation.viewmodel.QuizViewModel as SharedQuizViewModel

// Android lifecycle adapter for the shared QuizViewModel.
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
    val questions = sharedViewModel.questions
    val currentQuestion get() = sharedViewModel.currentQuestion
    val questionCount get() = sharedViewModel.questionCount
    val isResultSaved = sharedViewModel.isResultSaved

    fun selectAnswer(answerIndex: Int) {
        sharedViewModel.selectAnswer(answerIndex)
    }

    fun nextQuestion(): Boolean {
        return sharedViewModel.nextQuestion()
    }

    fun startQuiz(difficulty: com.razegon.shared.domain.model.QuizDifficulty) {
        sharedViewModel.startQuiz(difficulty)
    }

    fun restartQuiz() {
        sharedViewModel.restartQuiz()
    }

    fun markResultAsSaved() {
        sharedViewModel.markResultAsSaved()
    }
}
