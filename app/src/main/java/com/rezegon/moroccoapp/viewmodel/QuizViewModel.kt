package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.moroccoapp.domain.model.QuizDifficulty
import com.rezegon.moroccoapp.domain.model.QuizQuestion
import com.rezegon.moroccoapp.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    // Questions are loaded when the user selects the quiz difficulty.
    private var questions: List<QuizQuestion> = emptyList()

    // Index of the question currently displayed.
    // Starts with the first question (index 0).
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    // Index of the answer selected by the user.
    // Null means that no answer has been selected yet.
    private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
    val selectedAnswerIndex: StateFlow<Int?> = _selectedAnswerIndex

    // Number of correctly answered questions in the current quiz.
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    // Stores the difficulty selected for the current quiz.
    private val _selectedDifficulty = MutableStateFlow<QuizDifficulty?>(null)
    val selectedDifficulty: StateFlow<QuizDifficulty?> = _selectedDifficulty

    // Becomes true after the user selects an answer.
    // This locks the question until the user moves to the next one.
    private val _isAnswerChecked = MutableStateFlow(false)
    val isAnswerChecked: StateFlow<Boolean> = _isAnswerChecked

    // Returns the question corresponding to the current index.
    val currentQuestion: QuizQuestion
        get() = questions[_currentQuestionIndex.value]

    val questionCount: Int
        get() = questions.size

    private val _isResultSaved = MutableStateFlow(false)
    val isResultSaved: StateFlow<Boolean> = _isResultSaved

    fun selectAnswer(answerIndex: Int) {
        // Do nothing when an answer has already been selected.
        if (_isAnswerChecked.value) return

        _selectedAnswerIndex.value = answerIndex
        _isAnswerChecked.value = true

        if (answerIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 1
        }
    }

    fun nextQuestion(): Boolean {
        // We can move forward only after an answer was selected.
        if (!_isAnswerChecked.value) return false

        return if (_currentQuestionIndex.value < questions.lastIndex) {
            _currentQuestionIndex.value += 1
            _selectedAnswerIndex.value = null
            _isAnswerChecked.value = false
            true
        } else {
            false
        }
    }

    // Starts a new quiz using questions matching the selected difficulty.
    fun startQuiz(difficulty: QuizDifficulty) {
        _selectedDifficulty.value = difficulty
        questions = repository.getQuestions(difficulty)
    }

    // Resets the quiz state so the user can start a new quiz.
    fun restartQuiz() {
        _currentQuestionIndex.value = 0
        _selectedAnswerIndex.value = null
        _score.value = 0
        _isAnswerChecked.value = false
        _isResultSaved.value = false
        _selectedDifficulty.value = null
    }

    fun markResultAsSaved() {
        _isResultSaved.value = true
    }
}

