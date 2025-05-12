package com.misfigus.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.misfigus.dto.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object AuthApi {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}
