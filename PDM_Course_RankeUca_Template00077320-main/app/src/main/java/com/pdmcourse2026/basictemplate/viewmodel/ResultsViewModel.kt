package com.pdmcourse2026.basictemplate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmcourse2026.basictemplate.data.remote.PlaceDto
import com.pdmcourse2026.basictemplate.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ResultsState {
    object Loading : ResultsState()
    data class Success(val places: List<PlaceDto>) : ResultsState()
    data class Error(val message: String) : ResultsState()
}

class ResultsViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _state = MutableStateFlow<ResultsState>(ResultsState.Loading)
    val state: StateFlow<ResultsState> = _state.asStateFlow()

    init {
        fetchResults()
    }

    fun fetchResults() {
        viewModelScope.launch {
            _state.value = ResultsState.Loading
            repository.getPlaces()
                .onSuccess { places ->
                    // Ordenamos de mayor a menor
                    val sortedPlaces = places.sortedByDescending { it.votes ?: 0 }
                    _state.value = ResultsState.Success(sortedPlaces)
                }
                .onFailure { error ->
                    _state.value = ResultsState.Error(error.message ?: "Error al cargar resultados")
                }
        }
    }
}
