package com.pdmcourse2026.basictemplate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmcourse2026.basictemplate.data.remote.PlaceDto
import com.pdmcourse2026.basictemplate.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val places: List<PlaceDto>, val votedPlaceId: Int? = null) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            repository.getPlaces()
                .onSuccess { places ->
                    _state.value = HomeState.Success(places)
                }
                .onFailure { error ->
                    _state.value = HomeState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun vote(placeId: Int, carnet: String = "00077320") {
        val currentState = _state.value
        if (currentState is HomeState.Success) {
            viewModelScope.launch {
                // Actualizamos el ID votado en el estado actual para que se marque en la UI
                _state.value = currentState.copy(votedPlaceId = placeId)
                
                // Llamamos al repositorio con el carnet
                repository.voteForPlace(placeId, carnet)
                    .onFailure { error ->
                        // Si falla, mostramos el error
                        _state.value = HomeState.Error(error.message ?: "Error al registrar el voto")
                    }
            }
        }
    }
}
