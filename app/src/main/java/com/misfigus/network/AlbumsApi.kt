package com.misfigus.network

import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.misfigus.dto.AlbumCategoryCountDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object AlbumsApi {
    private const val BASE_URL = "http://10.0.2.2:8080/albums/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: AlbumsApiService by lazy {
        retrofit.create(AlbumsApiService::class.java)
    }

}

interface AlbumsApiService {
    @GET("count-by-category")
    suspend fun getData(): List<AlbumCategoryCountDto>
}

