package com.misfigus.network

import com.misfigus.dto.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    @POST("users/register")
    suspend fun register(@Body user: UserRegisterDto): LoginResponseDto

    @POST("users/login")
    suspend fun login(@Body credentials: UserLoginDto): LoginResponseDto

    @GET("users/me")
    suspend fun getCurrentUser(): UserDto

    @Multipart
    @POST("users/upload-profile-image")
    suspend fun uploadProfileImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): ImageUploadResponseDto
}
