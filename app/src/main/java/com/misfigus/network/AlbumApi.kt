package com.misfigus.network

object AlbumApi {
    val retrofitService: AlbumApiService by lazy {
        RetrofitClient.create(AlbumApiService::class.java)
    }
}
