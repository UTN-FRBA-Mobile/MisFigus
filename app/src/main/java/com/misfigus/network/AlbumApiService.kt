package com.misfigus.network

import com.misfigus.dto.AlbumCategoryCountDto
import retrofit2.http.GET
import retrofit2.http.Header

interface AlbumApiService {

    @GET("albums/count-by-category")
    suspend fun getAlbums(): List<AlbumCategoryCountDto>
}