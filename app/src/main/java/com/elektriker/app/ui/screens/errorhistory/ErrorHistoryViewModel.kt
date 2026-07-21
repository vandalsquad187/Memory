package com.elektriker.app.ui.screens.errorhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektriker.app.data.local.entity.ErrorCauseEntity
import com.elektriker.app.data.local.entity.ErrorLogEntity
import com.elektriker.app.data.repository.ErrorCauseRepository
import com.elektriker.app.data.repository.ErrorLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ErrorHistoryUiState(
    val errors: List<ErrorLogEntity> = emptyList(),
    val causeLabels: Map<String, String> = emptyMap()
)

@HiltViewModel
class ErrorHistoryViewModel @Inject constructor(
    private val errorLogRepository: ErrorLogRepository,
    private val errorCauseRepository: ErrorCauseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ErrorHistoryUiState())
    val state: StateFlow<ErrorHistoryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            errorLogRepository.getAllErrors().collect { errors ->
                _state.update { it.copy(errors = errors) }
            }
        }
        viewModelScope.launch {
            errorCauseRepository.getAllCauses().collect { causes ->
                val labels = causes.associate { it.id to it.label }
                _state.update { it.copy(causeLabels = labels) }
            }
        }
    }
}
