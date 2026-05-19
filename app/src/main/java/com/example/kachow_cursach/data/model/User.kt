package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: Int?,
    val username: String?,
    val role: String?,
    val email: String? = null,
    val createdAt: Long? = null
)

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String?,
    val role: String,
    val createdAt: Long?
)