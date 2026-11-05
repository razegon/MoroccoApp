package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.model.ArcadeResult
import com.razegon.shared.presentation.viewmodel.ArcadeResultViewModel as SharedArcadeResultViewModel
import kotlinx.coroutines.launch

// Android lifecycle adapter for the shared ArcadeResultViewModel.
class ArcadeResultAndroidViewModel(
    private val sharedViewModel: SharedArcadeResultViewModel
) : ViewModel() {

    val topResults = sharedViewModel.topResults

    fun saveResult(result: ArcadeResult) {
        viewModelScope.launch {
            sharedViewModel.saveResult(result)
        }
    }

    fun loadTopResults() {
        viewModelScope.launch {
            sharedViewModel.loadTopResults()
        }
    }
}
