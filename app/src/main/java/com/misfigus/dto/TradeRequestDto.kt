package com.misfigus.dto

import com.misfigus.models.trades.TradeRequestStatus
import kotlinx.serialization.Serializable

@Serializable
data class TradeRequestDto(
    val album: Int,
    val albumName: String,
    val from: UserDto,
    val to: UserDto,
    val stickers: List<Int>,
    val toGive: List<Int>,
    val status: TradeRequestStatus
)

@Serializable
data class PossibleTradeDto(
    val album: Int,
    val albumName: String,
    val from: UserDto,
    val stickers: List<Int>,
    val toGive: List<Int>
)
