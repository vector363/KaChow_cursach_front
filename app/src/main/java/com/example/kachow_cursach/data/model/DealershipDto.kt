package com.example.kachow_cursach.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DealershipDto(
    val id: Int,
    val name: String,
    val address: String,
    val rating: String,
    val carCount: Int? = 0,
    val imageUrl: String? = null
)