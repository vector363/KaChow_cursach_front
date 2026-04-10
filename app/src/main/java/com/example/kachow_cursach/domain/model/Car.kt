package com.example.kachow_cursach.domain.model

data class Car(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Int,
    val year: String,
    val mileage: Int,
    val imageRes: Int?,
    val isFavorite: Boolean = false,

    // Новые поля для детального экрана
    val engine: String = "3.0 V6",
    val horsepower: Int = 400,
    val transmission: String = "Автомат",
    val driveUnit: String = "Полный",
    val color: String = "Красный",
    val description: String = "Автомобиль в отличном состоянии. Полный сервис у официального дилера. Без ДТП."
)