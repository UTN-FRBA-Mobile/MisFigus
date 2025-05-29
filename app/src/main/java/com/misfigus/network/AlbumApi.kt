package com.misfigus.network

import android.content.Context

object AlbumApi {
    fun getService(context: Context): AlbumApiService {
        return RetrofitClient.create(AlbumApiService::class.java, context)
    }
}
