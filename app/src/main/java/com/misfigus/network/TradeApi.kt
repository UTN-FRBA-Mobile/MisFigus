package com.misfigus.network

import android.content.Context

object TradeApi {
    fun getService(context: Context): TradeApiService {
        return RetrofitClient.create(TradeApiService::class.java, context)
    }
}
