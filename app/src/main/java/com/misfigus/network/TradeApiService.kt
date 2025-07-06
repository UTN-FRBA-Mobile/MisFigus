package com.misfigus.network

import com.misfigus.dto.PossibleTradeDto
import com.misfigus.dto.TradeRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TradeApiService {
    @GET("trade-requests/get-all")
    suspend fun getAllTradeRequests(): List<TradeRequestDto>

    @GET("users/possible-trades")
    suspend fun getPossibleTrades(): List<PossibleTradeDto>

    @POST("trade-requests/new")
    suspend fun postNewTradeRequest(@Body request: TradeRequestDto)

}