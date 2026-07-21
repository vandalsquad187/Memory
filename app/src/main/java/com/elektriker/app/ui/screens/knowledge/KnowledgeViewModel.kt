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
    val showFavoritesOnly: Boolean = false,
    val editingEntry: KnowledgeBaseEntity? = null
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

    fun createEntry(title: String, content: String, tags: String, category: String) {
        viewModelScope.launch {
            knowledgeRepository.createEntry(
                title = title,
                content = content,
                tags = tags,
                category = category
            )
        }
    }

    fun openEntry(entry: KnowledgeBaseEntity) {
        _state.update { it.copy(editingEntry = entry) }
    }

    fun closeEntry() {
        _state.update { it.copy(editingEntry = null) }
    }

    fun updateEntry(id: String, title: String, content: String, tags: String, category: String) {
        viewModelScope.launch {
            knowledgeRepository.updateEntry(id, title, content, tags, category)
            closeEntry()
        }
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            knowledgeRepository.deleteEntry(id)
            closeEntry()
        }
    }
}
