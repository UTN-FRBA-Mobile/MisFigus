package com.misfigus.network

import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.models.Album
import retrofit2.http.GET
import retrofit2.http.Header

interface AlbumApiService {

    @GET("albums/count-by-category")
    suspend fun getAlbumCountByCategory(): List<AlbumCategoryCountDto>

    @GET("albums/category")
    suspend fun getAlbumsCategory(@Query("categoryId") categoryId: String? = null): List<Album>

    @GET("albums")
    suspend fun getAlbum(@Query("albumId") albumId: String? = null): Album
}