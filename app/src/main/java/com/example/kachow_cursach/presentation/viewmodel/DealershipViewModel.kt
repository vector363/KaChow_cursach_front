package com.example.kachow_cursach.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kachow_cursach.data.model.DealershipDto
import com.example.kachow_cursach.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DealershipViewModel(
    private val repository: MainRepository
) : ViewModel(){

    private val _dealerships = MutableStateFlow<List<DealershipDto>>(emptyList())
    val dealerships: StateFlow<List<DealershipDto>> = _dealerships.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadDealerships() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getDealerships()
            result.fold(
                onSuccess = { _dealerships.value = it },
                onFailure = { _error.value = it.message }
            )
            _isLoading.value = false
        }
    }
}