package com.misfigus.dto

import com.misfigus.models.trades.Sticker
import com.misfigus.models.trades.TradeRequestStatus
import kotlinx.serialization.Serializable

@Serializable
data class TradeRequestDto(
    val id: String,
    val fromUserEmail: String,
    val toUserEmail: String,
    val status: TradeRequestStatus,
    val offeredStickers: List<Sticker>,
    val requestedStickers: List<Sticker>,
    val seen: Boolean
)

@Serializable
data class PossibleTradeDto(
    val album: Int,
    val albumName: String,
    val from: UserDto,
    val stickers: List<Integer>
)
