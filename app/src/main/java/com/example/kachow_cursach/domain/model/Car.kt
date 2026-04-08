package com.example.kachow_cursach.domain.model

data class Car(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Int,
    val year: String,
    val mileage: Int,
    val imageRes: Int?,      // ресурс фото (R.drawable...)
    val isFavorite: Boolean = false
)