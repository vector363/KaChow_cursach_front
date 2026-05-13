package com.example.kachow_cursach.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kachow_cursach.data.repository.MainRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MainRepository
) : ViewModel() {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onError(it.message ?: "Ошибка входа") }
            )
        }
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.register(username, email, password)
            result.fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = {
                    onError(it.message ?: "Ошибка регистрации")
                }
            )
        }
    }

    fun isAdmin(): Boolean {
        return repository.isAdmin()
    }

    fun getUserRole(): String {
        return repository.getUserRole()
    }


}
