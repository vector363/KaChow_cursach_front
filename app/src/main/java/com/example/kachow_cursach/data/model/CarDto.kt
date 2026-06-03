package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CarDto(
    val id: Int,
    val dealershipId: Int,
    val brand: String,
    val model: String,
    val price: Int? = null,
    val year: Int? = null,
    val mileage: Int? = null,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,


    val horsepower: Int? = null,
    val transmission: String? = null,
    val driveUnit: String? = null
)