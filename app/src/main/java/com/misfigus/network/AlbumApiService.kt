package com.misfigus.network

import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.models.Album
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AlbumApiService {

    @GET("albums/count-by-category")
    suspend fun getAlbumCountByCategory(): List<AlbumCategoryCountDto>

    @GET("albums/category")
    suspend fun getAlbumsCategory(@Query("categoryId") categoryId: String? = null): List<Album>

    @GET("albums/{albumId}")
    suspend fun getAlbum(@Query("albumId") albumId: String? = null): Album

    @GET("albums")
    suspend fun getAlbums(): List<Album>
}