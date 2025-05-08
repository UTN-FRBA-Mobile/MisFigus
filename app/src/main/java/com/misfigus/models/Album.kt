package com.misfigus.models

data class Album(
    var albumId: String,
    var tradingCards: List<TradingCard>,
    var completo: Boolean
)