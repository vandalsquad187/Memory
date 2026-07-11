package com.elektriker.app.ui.screens.knowledge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektriker.app.data.local.entity.KnowledgeBaseEntity
import com.elektriker.app.data.repository.KnowledgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class KnowledgeUiState(
    val entries: List<KnowledgeBaseEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val showFavoritesOnly: Boolean = false
)

@HiltViewModel
class KnowledgeViewModel @Inject constructor(
    private val knowledgeRepository: KnowledgeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(KnowledgeUiState())
    val state: StateFlow<KnowledgeUiState> = _state.asStateFlow()

    init {
        loadAllEntries()
        viewModelScope.launch {
            knowledgeRepository.getAllCategories().collect { cats ->
                _state.update { it.copy(categories = cats) }
            }
        }
    }

    private fun loadAllEntries() {
        viewModelScope.launch {
            knowledgeRepository.getAllEntries().collect { entries ->
                _state.update { applyFilters(entries) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        if (query.isBlank()) {
            loadAllEntries()
        } else {
            viewModelScope.launch {
                knowledgeRepository.searchEntries(query).collect { entries ->
                    _state.update { applyFilters(entries) }
                }
            }
        }
    }

    fun selectCategory(category: String?) {
        _state.update {
            it.copy(selectedCategory = if (it.selectedCategory == category) null else category)
        }
    }

    fun toggleFavoritesFilter() {
        _state.update { it.copy(showFavoritesOnly = !it.showFavoritesOnly) }
    }

    private fun applyFilters(entries: List<KnowledgeBaseEntity>): KnowledgeUiState {
        val s = _state.value
        var filtered = entries
        if (s.selectedCategory != null) {
            filtered = filtered.filter { it.category == s.selectedCategory }
        }
        if (s.showFavoritesOnly) {
            filtered = filtered.filter { it.isFavorite }
        }
        return s.copy(entries = filtered)
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            knowledgeRepository.toggleFavorite(id, isFavorite)
        }
    }
}
