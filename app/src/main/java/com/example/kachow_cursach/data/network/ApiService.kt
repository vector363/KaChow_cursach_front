package com.example.kachow_cursach.data.network

import com.example.kachow_cursach.data.model.AuthResponse
import com.example.kachow_cursach.data.model.ErrorResponse
import com.example.kachow_cursach.data.model.LoginRequest
import com.example.kachow_cursach.data.model.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
        println(">>> RAW REGISTER RESPONSE: $rawResponse")

        val jsonElement = json.parseToJsonElement(rawResponse)
        if (jsonElement.jsonObject.containsKey("error")) {
            val errorResponse = json.decodeFromString<ErrorResponse>(rawResponse)
            throw Exception(errorResponse.error)
        }
        return json.decodeFromString<AuthResponse>(rawResponse)
    }
}