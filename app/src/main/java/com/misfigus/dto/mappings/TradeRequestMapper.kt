package com.misfigus.dto.mappings

import com.misfigus.dto.TradeRequestDto
import com.misfigus.models.trades.TradeRequest

object TradeRequestMapper {
    fun fromDto(dto: TradeRequestDto) = TradeRequest(
        id = dto.id,
        fromUserEmail = dto.fromUserEmail,
        toUserEmail = dto.toUserEmail,
        status = dto.status,
        offeredStickers = dto.offeredStickers,
        requestedStickers = dto.requestedStickers,
        seen = dto.seen
    )
}