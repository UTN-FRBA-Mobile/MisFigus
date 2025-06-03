package com.misfigus.network

import android.content.Context

object TradeRequestApi {
    fun getService(context: Context): TradeRequestApiService {
        return RetrofitClient.create(TradeRequestApiService::class.java, context)
    }
}
