package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.moroccoapp.domain.model.ArcadeResult
import com.rezegon.moroccoapp.domain.repository.ArcadeResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArcadeResultViewModel(
    private val repository: ArcadeResultRepository
) : ViewModel() {

    // Holds the current TOP 10 Arcade results for the UI.
    private val _topResults = MutableStateFlow<List<ArcadeResult>>(emptyList())
    val topResults: StateFlow<List<ArcadeResult>> = _topResults

    // Saves a completed Arcade game result.
    fun saveResult(result: ArcadeResult) {
        viewModelScope.launch {
            repository.saveResult(result)
        }
    }

    // Loads the best Arcade results from local storage.
    fun loadTopResults(limit: Int = 10) {
        viewModelScope.launch {
            _topResults.value = repository.getTopResults(limit)
        }
    }
}