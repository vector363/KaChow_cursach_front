package com.example.kachow_cursach.data.repository

import com.example.kachow_cursach.data.local.TokenManager
import com.example.kachow_cursach.data.model.AuthResponse
import com.example.kachow_cursach.data.model.CarDto
import com.example.kachow_cursach.data.model.DealershipDto
import com.example.kachow_cursach.data.network.ApiService


class MainRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(email, password)
            tokenManager.saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(username, email, password)
            tokenManager.saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clearToken()
    }

    suspend fun getDealerships(): Result<List<DealershipDto>>{
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = apiService.getDealerships(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCarsByDealership(dealershipId: Int): Result<List<CarDto>> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = apiService.getCarsByDealership(token, dealershipId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavorites(): Result<List<CarDto>> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = apiService.getFavorites(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToFavorites(carId: Int): Result<Unit> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            apiService.addToFavorites(token, carId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromFavorites(carId: Int): Result<Unit> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            apiService.removeFromFavorites(token, carId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}