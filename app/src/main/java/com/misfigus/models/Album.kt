package com.misfigus.models

import com.misfigus.models.trades.TradingCard
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    var id: Int,
    var albumId: String,
    var name: String,
    var tradingCards: List<TradingCard>,
    var finished: Boolean,
    var category: AlbumCategoryEnum,
    var cover: String,
    var releaseYear: Int,
    var totalCards: Int
)