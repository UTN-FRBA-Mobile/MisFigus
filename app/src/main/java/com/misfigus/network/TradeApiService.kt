package com.misfigus.network

import com.misfigus.dto.PossibleTradeDto
import com.misfigus.dto.TradeRequestDto
import retrofit2.http.GET

interface TradeApiService {
    @GET("trade-requests/get-all")
    suspend fun getAllTradeRequests(): List<TradeRequestDto>

    @GET("users/possible-trades")
    suspend fun getPossibleTrades(): List<PossibleTradeDto>

}