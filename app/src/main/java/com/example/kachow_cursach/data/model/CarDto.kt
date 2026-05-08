package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CarDto(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Int,
    val year: Int,
    val mileage: Int,
    val imageUrl: String?,
    val dealershipId: Int,
    val isFavorite: Boolean
)