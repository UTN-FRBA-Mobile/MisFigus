package com.misfigus.screens.trades.requests

import com.misfigus.models.trades.*

object MockTradeRequests {
    val allRequests = listOf(
        // Enviada por pedrito - PENDING
        TradeRequest(
            id = "1",
            fromUserEmail = "marcos@gmail.com",
            toUserEmail = "martina@gmail.com",
            status = TradeRequestStatus.PENDING,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "AR", "03"),
                Sticker("Fifa World Cup Qatar 2022", "BR", "10"),
                Sticker("Fifa World Cup Qatar 2022", "AU", "06"),
                Sticker("Fifa World Cup Qatar 2022", "CO", "01")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "AR", "01"),
                Sticker("Fifa World Cup Qatar 2022", "NZ", "07"),
                Sticker("Fifa World Cup Qatar 2022", "GR", "07"),
                Sticker("Fifa World Cup Qatar 2022", "UY", "03")
            ),
            seen = false
        ),

        // Recibida por pedrito - REJECTED
        TradeRequest(
            id = "2",
            fromUserEmail = "marcos@gmail.com",
            toUserEmail = "pedrito@gmail.com",
            status = TradeRequestStatus.REJECTED,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "FR", "05")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "MX", "12")
            ),
            seen = false
        ),

        // Enviada por pedrito - ACCEPTED
        TradeRequest(
            id = "3",
            fromUserEmail = "marcos@gmail.com",
            toUserEmail = "carlos@gmail.com",
            status = TradeRequestStatus.ACCEPTED,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "ES", "09")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "JP", "05")
            ),
            seen = false
        ),

        // Recibida por pedrito - PENDING
        TradeRequest(
            id = "4",
            fromUserEmail = "lucas@gmail.com",
            toUserEmail = "marcos@gmail.com",
            status = TradeRequestStatus.PENDING,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "IT", "02")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "AR", "10")
            ),
            seen = false
        ),

        // Recibida por pedrito - ACCEPTED
        TradeRequest(
            id = "5",
            fromUserEmail = "nicolas@gmail.com",
            toUserEmail = "marcos@gmail.com",
            status = TradeRequestStatus.ACCEPTED,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "DE", "06")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "BR", "07")
            ),
            seen = false
        ),

        // Enviada por pedrito - REJECTED
        TradeRequest(
            id = "6",
            fromUserEmail = "martina@gmail.com",
            toUserEmail = "marcos@gmail.com",
            status = TradeRequestStatus.REJECTED,
            offeredStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "PT", "11")
            ),
            requestedStickers = listOf(
                Sticker("Fifa World Cup Qatar 2022", "CA", "04")
            ),
            seen = false
        )
    )
}
