package com.misfigus.network

import com.misfigus.dto.KioskDTO
import retrofit2.http.GET

interface KioskApiService {
    @GET("kiosks")
    suspend fun getKiosks(): List<KioskDTO>
}
