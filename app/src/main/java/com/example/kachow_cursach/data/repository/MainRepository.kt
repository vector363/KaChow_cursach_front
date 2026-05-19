package com.example.kachow_cursach.data.repository

import com.example.kachow_cursach.data.local.TokenManager
import com.example.kachow_cursach.data.model.AddCarRequest
import com.example.kachow_cursach.data.model.AddCarResponse
import com.example.kachow_cursach.data.model.AuthResponse
import com.example.kachow_cursach.data.model.CarDetailResponse
import com.example.kachow_cursach.data.model.CarDto
import com.example.kachow_cursach.data.model.CarImageDto
import com.example.kachow_cursach.data.model.DealershipDto
import com.example.kachow_cursach.data.model.UpdateCarRequest
import com.example.kachow_cursach.data.model.UpdateCarResponse
import com.example.kachow_cursach.data.model.User
import com.example.kachow_cursach.data.network.ApiService


class MainRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(email, password)
            tokenManager.saveAuthData(
                token = response.token,
                userId = response.userId,
                username = response.username,
                email = response.email,
                role = response.role
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(username, email, password)
            tokenManager.saveAuthData(
                token = response.token,
                userId = response.userId,
                username = response.username,
                email = response.email,
                role = response.role
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserRole(): String {
        return tokenManager.getUserRole()
    }

    fun isAdmin(): Boolean {
        return tokenManager.isAdmin()
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

    suspend fun getCarImages(carId: Int): Result<List<CarImageDto>> {
        val token = tokenManager.getToken()
        return try {
            if (token == null) {
                return Result.failure(Exception("Not authenticated"))
            }
            val images = apiService.getCarImages(token, carId)
            Result.success(images)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCarDetail(carId: Int): Result<CarDetailResponse> {
        val token = tokenManager.getToken()
        println(">>> [REPO] getCarDetail START for carId=$carId")
        println(">>> [REPO] Token exists: ${token != null}")

        if (token == null) {
            println(">>> [REPO] ERROR: No token found")
            return Result.failure(Exception("Not authenticated"))
        }

        return try {
            println(">>> [REPO] Calling apiService.getCarDetail...")
            val response = apiService.getCarDetail(token, carId)
            println(">>> [REPO] SUCCESS: ${response.brand} ${response.model}")
            Result.success(response)
        } catch (e: Exception) {
            println(">>> [REPO] ERROR: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
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
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val request = AddCarRequest(
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
            val response = apiService.addCar(token, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
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
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val request = UpdateCarRequest(
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
            val response = apiService.updateCar(token, carId, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCarById(carId: Int): Result<CarDetailResponse> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = apiService.getCarById(token, carId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val userResponse = apiService.getCurrentUser(token)
            println(">>> [REPO] SUCCESS: ${userResponse}")
            if (userResponse.userId != null) {
                Result.success(
                    User(
                        id = userResponse.userId,
                        username = userResponse.username ?: "Пользователь",
                        email = userResponse.email,
                        role = userResponse.role ?: "user",
                        createdAt = userResponse.createdAt
                    )
                )
            } else {
                Result.failure(Exception("Не удалось получить данные пользователя"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun clearToken() {
        tokenManager.clearToken()
    }
}