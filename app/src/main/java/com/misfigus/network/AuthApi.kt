package com.misfigus.network

object AuthApi {
    val retrofitService: AuthApiService by lazy {
        RetrofitClient.create(AuthApiService::class.java)
    }
}

