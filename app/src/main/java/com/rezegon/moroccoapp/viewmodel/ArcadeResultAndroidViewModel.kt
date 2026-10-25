
package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.model.ArcadeResult
import com.rezegon.shared.presentation.viewmodel.ArcadeResultViewModel as SharedArcadeResultViewModel
import kotlinx.coroutines.launch

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
