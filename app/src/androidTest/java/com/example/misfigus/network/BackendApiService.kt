import com.misfigus.models.Album
import retrofit2.Retrofit

import retrofit2.Call
import retrofit2.http.GET

/*package com.example.misfigus.network
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com";

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

public interface BackendApiService {
    @GET("albums")
    suspend fun getAlbums(): String
}

object BackendApi {
    val retrofitService : BackendApiService by lazy {
        retrofit.create(BackendApiService::class.java)
    }

}*/


interface ApiService {
    @GET("albums")  // Adjust path to your endpoint
    fun getData(): Call<Album>
}

