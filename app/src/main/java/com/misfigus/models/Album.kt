package com.misfigus.models

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    var albumId: String,
    var tradingCards: List<TradingCard>,
    var finished: Boolean,
    var category: AlbumCategoryEnum
)