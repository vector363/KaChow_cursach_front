package com.example.kachow_cursach.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kachow_cursach.data.model.CarDto
import com.example.kachow_cursach.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CarViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _cars = MutableStateFlow<List<CarDto>>(emptyList())
    val cars: StateFlow<List<CarDto>> = _cars.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCars(dealershipId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getCarsByDealership(dealershipId)
            result.fold(
                onSuccess = {
                    _cars.value = it
                },
                onFailure = {
                    _error.value = it.message
                }
            )
            _isLoading.value = false
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getFavorites()
            result.fold(
                onSuccess = {
                    _cars.value = it
                },
                onFailure = {
                    _error.value = it.message
                }
            )
            _isLoading.value = false
        }
    }

    fun toggleFavorite(carId: Int, isCurrentlyFavorite: Boolean, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = if (isCurrentlyFavorite) {
                repository.removeFromFavorites(carId)
            } else {
                repository.addToFavorites(carId)
            }
            result.fold(
                onSuccess = { onComplete(true) },
                onFailure = { onComplete(false) }
            )
        }
    }

    fun clearError() {
        _error.value = null
    }
}