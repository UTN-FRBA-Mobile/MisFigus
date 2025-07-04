package com.misfigus.dto

import kotlinx.serialization.Serializable

@Serializable
data class KioskDTO(
    val id: String,
    val name: String,
    val address: String,
    val responsible: String,
    val coordinates: Coordinates,
    val rating: Double,
    val openFrom: String,
    val openUntil: String,
    val availableUnits: Int,
    val stock: Boolean,
    val price: Int
)
