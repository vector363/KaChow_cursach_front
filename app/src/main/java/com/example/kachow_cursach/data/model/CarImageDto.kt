package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CarImageDto(
    val id: Int,
    val carId: Int,
    val imageUrl: String,
    val isPreview: Boolean,
    val orderIndex: Int
)