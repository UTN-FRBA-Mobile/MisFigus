package com.misfigus.dto

import com.misfigus.models.trades.TradeRequestStatus
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TradeRequestDto(
    val id: String,
    val album: Int,
    val albumName: String,
    val from: UserDto,
    val to: UserDto,
    val stickers: List<Int>,
    val toGive: List<Int>,
    val status: TradeRequestStatus
)

fun TradeRequestDto.getIdAsUUID(): UUID = UUID.fromString(id)

@Serializable
data class PossibleTradeDto(
    val album: Int,
    val albumName: String,
    val from: UserDto,
    val stickers: List<Int>,
    val toGive: List<Int>
)
