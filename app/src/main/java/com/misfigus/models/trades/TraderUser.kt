package com.misfigus.models.trades

data class TraderUser(
    val userId: String,
    val userName: String,
    val profileImageUrl: String,
    val location: String,
    val ships: Boolean,
    val reputation: String // "Muy mala", "Mala", "Aceptable", "Buena", "Muy buena"
)