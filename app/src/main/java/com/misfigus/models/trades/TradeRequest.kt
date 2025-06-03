package com.misfigus.models.trades

data class TradeRequest(
    val id: String,
    val fromUserEmail: String,
    val toUserEmail: String,
    val status: TradeRequestStatus,
    val offeredStickers: List<Sticker>,
    val requestedStickers: List<Sticker>,
    var seen: Boolean = false
)
