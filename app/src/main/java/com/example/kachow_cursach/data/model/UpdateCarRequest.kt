package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCarRequest(
    val brand: String,
    val model: String,
    val price: Int,
    val year: Int,
    val mileage: Int,
    val engine: String,
    val horsepower: Int,
    val transmission: String,
    val driveUnit: String,
    val color: String,
    val description: String,
    val imageUrl: String? = null,
    val dealershipId: Int
)

@Serializable
data class UpdateCarResponse(
    val id: Int,
    val brand: String,
    val model: String,
    val message: String = "Автомобиль успешно обновлен"
)