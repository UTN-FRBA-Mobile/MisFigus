package com.misfigus.models.trades

import kotlinx.serialization.Serializable

@Serializable
data class TradingCard (
    var number: Int,
    var albumId: String,
    var obtained: Boolean,
    var repeatedQuantity: Int
)