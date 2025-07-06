package com.misfigus.network

import com.misfigus.dto.PossibleTradeDto
import com.misfigus.dto.TradeRequestDto
import com.misfigus.models.trades.TradeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface TradeApiService {
    @GET("trade-requests/get-all")
    suspend fun getAllTradeRequests(): List<TradeRequestDto>

    @GET("users/possible-trades")
    suspend fun getPossibleTrades(): List<PossibleTradeDto>

    @POST("trade-requests/new")
    suspend fun postNewTradeRequest(@Body request: TradeRequestDto)

    @PUT("trade-requests/reject/{id}")
    suspend fun rejectTradeRequest(@Path("id") id: UUID)

    @PUT("trade-requests/accept")
    suspend fun acceptTradeRequest(@Body tradeRequest: TradeRequestDto)

}