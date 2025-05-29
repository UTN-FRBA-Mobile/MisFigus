package com.misfigus.network

import android.content.Context

object AuthApi {
    fun getService(context: Context): AuthApiService {
        return RetrofitClient.create(AuthApiService::class.java, context)
    }
}



