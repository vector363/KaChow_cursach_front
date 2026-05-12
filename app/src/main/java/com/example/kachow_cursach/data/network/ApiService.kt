package com.example.kachow_cursach.data.network

import com.example.kachow_cursach.data.model.AuthResponse
import com.example.kachow_cursach.data.model.CarDetailDto
import com.example.kachow_cursach.data.model.CarDetailResponse
import com.example.kachow_cursach.data.model.CarDto
import com.example.kachow_cursach.data.model.CarImageDto
import com.example.kachow_cursach.data.model.DealershipDto
import com.example.kachow_cursach.data.model.ErrorResponse
import com.example.kachow_cursach.data.model.LoginRequest
import com.example.kachow_cursach.data.model.RegisterRequest
import com.example.kachow_cursach.data.network.KtorClient.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject


class ApiService(
    private val client: HttpClient = KtorClient.client
) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return client.post("${KtorClient.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun register(username: String, email: String, password: String): AuthResponse {
        val response = client.post("${KtorClient.BASE_URL}/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username, email, password))
        }
        val rawResponse = response.body<String>()

        val jsonElement = json.parseToJsonElement(rawResponse)
        if (jsonElement.jsonObject.containsKey("error")) {
            val errorResponse = json.decodeFromString<ErrorResponse>(rawResponse)
            throw Exception(errorResponse.error)
        }
        return json.decodeFromString<AuthResponse>(rawResponse)
    }

    suspend fun getDealerships(token: String): List<DealershipDto> {
        return client.get("${KtorClient.BASE_URL}/dealership/all") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }

    suspend fun getCarsByDealership(token: String, dealershipId: Int): List<CarDto> {
        val response = client.get("${KtorClient.BASE_URL}/car/dealership/$dealershipId") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        val rawResponse = response.body<String>()
        return json.decodeFromString<List<CarDto>>(rawResponse)
    }

    suspend fun getFavorites(token: String): List<CarDto> {
        return client.get("${KtorClient.BASE_URL}/favorites") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }

    suspend fun addToFavorites(token: String, carId: Int) {
        client.post("${KtorClient.BASE_URL}/favorites/$carId") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
    }

    suspend fun removeFromFavorites(token: String, carId: Int) {
        client.delete("${KtorClient.BASE_URL}/favorites/$carId") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
    }

    suspend fun getCarImages(token: String, carId: Int): List<CarImageDto> {
        return client.get("${BASE_URL}/car/$carId/images") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }

    suspend fun getCarDetail(token: String, carId: Int): CarDetailResponse {
        println(">>> getCarDetail: calling for carId=$carId")
        return client.get("${BASE_URL}/car/$carId") {
            headers { append("Authorization", "Bearer $token") }
        }.body()
    }
}