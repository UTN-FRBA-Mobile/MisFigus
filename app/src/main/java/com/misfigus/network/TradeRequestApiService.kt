package com.misfigus.network

import com.misfigus.dto.TradeRequestDto
import retrofit2.http.GET

interface TradeRequestApiService {
    @GET("trade-requests/get-all")
    suspend fun getAllTradeRequests(): List<TradeRequestDto>
}