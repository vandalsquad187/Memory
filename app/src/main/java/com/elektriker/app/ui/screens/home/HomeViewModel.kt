package com.elektriker.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektriker.app.data.local.entity.WorkTaskEntity
import com.elektriker.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recentTasks: List<WorkTaskEntity> = emptyList(),
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val isTimerRunning: Boolean = false,
    val timerElapsedSeconds: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            taskRepository.getRecentTasks().collect { tasks ->
                _state.update { it.copy(recentTasks = tasks) }
            }
        }
        viewModelScope.launch {
            taskRepository.getTaskCount().collect { count ->
                _state.update { it.copy(totalTasks = count) }
            }
        }
        viewModelScope.launch {
            taskRepository.getCompletedTaskCount().collect { count ->
                _state.update { it.copy(completedTasks = count) }
            }
        }
    }

    fun startTimer() {
        _state.update { it.copy(isTimerRunning = true) }
    }

    fun pauseTimer() {
        _state.update { it.copy(isTimerRunning = false) }
    }

    fun tickTimer() {
        _state.update { it.copy(timerElapsedSeconds = it.timerElapsedSeconds + 1) }
    }

    fun resetTimer() {
        _state.update { it.copy(isTimerRunning = false, timerElapsedSeconds = 0) }
    }
}
