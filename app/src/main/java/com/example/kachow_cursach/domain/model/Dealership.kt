package com.example.kachow_cursach.domain.model


data class Dealership(
    val id: Int,
    val name: String,
    val address: String,
    val imageUrl: String? = null
)