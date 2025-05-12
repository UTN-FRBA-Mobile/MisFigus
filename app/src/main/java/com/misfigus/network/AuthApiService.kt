package com.misfigus.network

import com.misfigus.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("users/register")
    suspend fun register(@Body user: UserRegisterDto): LoginResponseDto

    @POST("users/login")
    suspend fun login(@Body credentials: UserLoginDto): LoginResponseDto

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): UserDto

}
