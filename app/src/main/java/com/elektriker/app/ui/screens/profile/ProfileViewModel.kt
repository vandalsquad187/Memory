package com.elektriker.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektriker.app.data.local.entity.WorkTaskEntity
import com.elektriker.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val completionRate: Float = 0f,
    val errorCount: Int = 0,
    val recentErrors: List<String> = emptyList(),
    val tasksByCategory: Map<String, Int> = emptyMap()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val errorLogRepository: ErrorLogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            taskRepository.getTaskCount().collect { total ->
                val completed = _state.value.completedTasks
                _state.update {
                    it.copy(
                        totalTasks = total,
                        completionRate = if (total > 0) completed.toFloat() / total else 0f
                    )
                }
            }
        }
        viewModelScope.launch {
            taskRepository.getCompletedTaskCount().collect { completed ->
                val total = _state.value.totalTasks
                _state.update {
                    it.copy(
                        completedTasks = completed,
                        completionRate = if (total > 0) completed.toFloat() / total else 0f
                    )
                }
            }
        }
        viewModelScope.launch {
            errorLogRepository.getAllErrors().collect { errors ->
                _state.update {
                    it.copy(
                        errorCount = errors.size,
                        recentErrors = errors.take(5).map { e -> e.description }
                    )
                }
            }
        }
        viewModelScope.launch {
            taskRepository.getAllTasks().collect { tasks ->
                val byCategory = tasks.groupBy { it.category }
                    .mapValues { it.value.size }
                _state.update { it.copy(tasksByCategory = byCategory) }
            }
        }
    }
}
