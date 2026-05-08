package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse (
    val error: String
)