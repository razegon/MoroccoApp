package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.QuizViewModel as SharedQuizViewModel

class QuizAndroidViewModel(
    private val sharedViewModel: SharedQuizViewModel
) : ViewModel() {

    val currentQuestionIndex = sharedViewModel.currentQuestionIndex
    val selectedAnswerIndex = sharedViewModel.selectedAnswerIndex
    val score = sharedViewModel.score
    val selectedDifficulty = sharedViewModel.selectedDifficulty
    val isAnswerChecked = sharedViewModel.isAnswerChecked
    val currentQuestion get() = sharedViewModel.currentQuestion
    val questionCount get() = sharedViewModel.questionCount
    val isResultSaved = sharedViewModel.isResultSaved

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
