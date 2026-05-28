package com.example.kachow_cursach.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kachow_cursach.data.model.AddCarResponse
import com.example.kachow_cursach.data.model.CarDetailResponse
import com.example.kachow_cursach.data.model.CarDto
import com.example.kachow_cursach.data.model.CarImageDto
import com.example.kachow_cursach.data.model.UpdateCarResponse
import com.example.kachow_cursach.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CarViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _carImagesMap = MutableStateFlow<Map<Int, List<CarImageDto>>>(emptyMap())
    val carImagesMap: StateFlow<Map<Int, List<CarImageDto>>> = _carImagesMap.asStateFlow()

    private val _carDetail = MutableStateFlow<CarDetailResponse?>(null)
    val carDetail: StateFlow<CarDetailResponse?> = _carDetail.asStateFlow()

    private val _cars = MutableStateFlow<List<CarDto>>(emptyList())
    val cars: StateFlow<List<CarDto>> = _cars.asStateFlow()

    private val _favorites = MutableStateFlow<List<CarDto>>(emptyList())
    val favorites: StateFlow<List<CarDto>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _carImages = MutableStateFlow<Map<Int, List<CarImageDto>>>(emptyMap())
    val carImages: StateFlow<Map<Int, List<CarImageDto>>> = _carImages.asStateFlow()

    fun loadCars(dealershipId: Int) {
        println(">>> loadCars called with dealershipId=$dealershipId")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getCarsByDealership(dealershipId)
            result.fold(
                onSuccess = { cars ->
                    println(">>> loadCars success: ${cars.size} cars loaded")
                    _cars.value = cars
                    updateFavoriteStatus(cars)
                },
                onFailure = {
                    println(">>> loadCars error: ${it.message}")
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
            try {
                val result = repository.getFavorites()
                if (result.isSuccess) {
                    val favoritesList = result.getOrNull() ?: emptyList()
                    _favorites.value = favoritesList
                    favoritesList.forEach { car ->
                        loadCarImages(car.id)
                    }
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorite(carId: Int, isCurrentlyFavorite: Boolean, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val result = if (isCurrentlyFavorite) {
                repository.removeFromFavorites(carId)
            } else {
                repository.addToFavorites(carId)
            }

            result.fold(
                onSuccess = {
                    val updatedCars = _cars.value.map { car ->
                        if (car.id == carId) car.copy(isFavorite = !isCurrentlyFavorite) else car
                    }
                    _cars.value = updatedCars

                    if (isCurrentlyFavorite) {
                        val newFavorites = _favorites.value.filter { it.id != carId }
                        _favorites.value = newFavorites
                    } else {
                        val carToAdd = updatedCars.find { it.id == carId }
                        if (carToAdd != null) {
                            val newFavorites = _favorites.value + carToAdd.copy(isFavorite = true)
                            _favorites.value = newFavorites
                        } else {
                            loadFavorites()
                        }
                    }

                    onComplete(true)
                },
                onFailure = { error ->
                    onComplete(false)
                }
            )
        }
    }


    private suspend fun updateFavoriteStatus(cars: List<CarDto>) {
        try {
            val favoritesResult = repository.getFavorites()
            if (favoritesResult.isSuccess) {
                val favoriteIds = favoritesResult.getOrNull()?.map { it.id } ?: emptyList()
                val updatedCars = cars.map { car ->
                    car.copy(isFavorite = favoriteIds.contains(car.id))
                }
                _cars.value = updatedCars
            }
        } catch (e: Exception) {
            println("Error updating favorite status: ${e.message}")
        }
    }

    fun loadCarImages(carId: Int) {
        viewModelScope.launch {
            val result = repository.getCarImages(carId)
            result.onSuccess { images ->
                _carImages.update { currentMap ->
                    currentMap + (carId to images)
                }
                println("Loaded ${images.size} images for car $carId")
            }.onFailure { error ->
                println("Failed to load images for car $carId: ${error.message}")
            }
        }
    }

    fun loadCarDetail(carId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getCarDetail(carId)
            result.fold(
                onSuccess = { carDetail ->
                    _carDetail.value = carDetail
                    loadCarImages(carId)
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )

            _isLoading.value = false
        }
    }


    suspend fun addCar(
        brand: String,
        model: String,
        price: Int,
        year: Int,
        mileage: Int,
        engine: String,
        horsepower: Int,
        transmission: String,
        driveUnit: String,
        color: String,
        description: String,
        imageUrl: String?,
        dealershipId: Int
    ): Result<AddCarResponse> {
        return repository.addCar(
            brand = brand,
            model = model,
            price = price,
            year = year,
            mileage = mileage,
            engine = engine,
            horsepower = horsepower,
            transmission = transmission,
            driveUnit = driveUnit,
            color = color,
            description = description,
            imageUrl = imageUrl,
            dealershipId = dealershipId
        )
    }

    private val _editingCar = MutableStateFlow<CarDetailResponse?>(null)
    val editingCar: StateFlow<CarDetailResponse?> = _editingCar.asStateFlow()

    fun loadCarForEditing(carId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getCarById(carId)
            result.fold(
                onSuccess = { car ->
                    _editingCar.value = car
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )

            _isLoading.value = false
        }
    }

    suspend fun updateCar(
        carId: Int,
        brand: String,
        model: String,
        price: Int,
        year: Int,
        mileage: Int,
        engine: String,
        horsepower: Int,
        transmission: String,
        driveUnit: String,
        color: String,
        description: String,
        imageUrl: String?,
        dealershipId: Int
    ): Result<UpdateCarResponse> {
        return repository.updateCar(
            carId = carId,
            brand = brand,
            model = model,
            price = price,
            year = year,
            mileage = mileage,
            engine = engine,
            horsepower = horsepower,
            transmission = transmission,
            driveUnit = driveUnit,
            color = color,
            description = description,
            imageUrl = imageUrl,
            dealershipId = dealershipId
        )
    }

    fun clearEditingCar() {
        _editingCar.value = null
    }
}


