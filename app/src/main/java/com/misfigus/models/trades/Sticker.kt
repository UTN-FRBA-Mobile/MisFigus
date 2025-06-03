package com.misfigus.models.trades

import kotlinx.serialization.Serializable

@Serializable
data class Sticker(
    val album: String,
    val name: String,
    val number: String
)
