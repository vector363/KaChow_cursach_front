package com.example.kachow_cursach.data.repository

import com.example.kachow_cursach.data.model.AuthResponse
import com.example.kachow_cursach.data.network.ApiService

class RemoteRepository(
    private val apiService: ApiService
) {

    suspend fun login(username: String, password: String): AuthResponse {
        return apiService.login(username, password)
    }

    suspend fun register(username: String, email: String, password: String): AuthResponse {
        return apiService.register(username, email, password)
    }

}