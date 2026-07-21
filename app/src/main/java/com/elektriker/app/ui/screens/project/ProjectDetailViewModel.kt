package com.elektriker.app.ui.screens.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektriker.app.data.local.entity.ProjectEntity
import com.elektriker.app.data.local.entity.WorkTaskEntity
import com.elektriker.app.data.repository.ProjectRepository
import com.elektriker.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectDetailUiState(
    val project: ProjectEntity? = null,
    val tasks: List<WorkTaskEntity> = emptyList(),
    val completedTasks: Int = 0,
    val totalTasks: Int = 0
)

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectDetailUiState())
    val state: StateFlow<ProjectDetailUiState> = _state.asStateFlow()

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            val project = projectRepository.getProjectById(projectId)
            _state.update { it.copy(project = project) }
        }
        viewModelScope.launch {
            taskRepository.getTasksByProjectId(projectId).collect { tasks ->
                _state.update {
                    it.copy(
                        tasks = tasks,
                        totalTasks = tasks.size,
                        completedTasks = tasks.count { t -> t.isCompleted }
                    )
                }
            }
        }
    }
}
