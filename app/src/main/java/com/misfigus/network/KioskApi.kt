package com.misfigus.network

import android.content.Context

object KioskApi {
    fun getService(context: Context): KioskApiService {
        return RetrofitClient.create(KioskApiService::class.java, context)
    }
}
