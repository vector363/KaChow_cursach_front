package com.example.kachow_cursach.presentation.model

import kotlinx.serialization.Serializable


@Serializable
data class CarFilters(
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minMileage: Int? = null,
    val maxMileage: Int? = null,
    val transmission: String? = null,
    val driveUnit: String? = null,
    val minHorsepower: Int? = null,
    val maxHorsepower: Int? = null
) {
    fun isNotEmpty(): Boolean {
        return minPrice != null || maxPrice != null ||
                minYear != null || maxYear != null ||
                minMileage != null || maxMileage != null ||
                transmission != null || driveUnit != null ||
                minHorsepower != null || maxHorsepower != null
    }
}