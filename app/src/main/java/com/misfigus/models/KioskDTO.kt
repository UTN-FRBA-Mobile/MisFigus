package com.misfigus.models

data class KioskDTO(
    val id: String,
    val name: String,
    val address: String,
    val coordinates: Coordinates,
    val rating: Double,
    val openUntil: String,
    val availableUnits: Int,
    val stock: Boolean,
    val price: Int
)