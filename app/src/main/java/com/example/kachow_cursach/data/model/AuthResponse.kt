package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val email: String,
    val role: String
)