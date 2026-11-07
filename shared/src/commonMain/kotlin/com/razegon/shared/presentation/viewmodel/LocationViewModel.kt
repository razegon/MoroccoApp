package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.UserLocationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel(
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(UserLocationState())
    val uiState: StateFlow<UserLocationState> = _uiState.asStateFlow()
}
