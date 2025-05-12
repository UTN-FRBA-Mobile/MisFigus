package com.misfigus.models

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    var albumId: String,
    var name: String,
    var tradingCards: List<TradingCard>,
    var finished: Boolean,
    var category: AlbumCategoryEnum,
    var cover: String,
    var releaseYear: Int,
    var totalCards: Int
)