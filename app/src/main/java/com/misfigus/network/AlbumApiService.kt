package com.misfigus.network

import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.models.Album
import com.misfigus.models.trades.TradingCard
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApiService {

    @GET("albums/count-by-category/{email}")
    suspend fun getAlbumCountByCategory(@Path("email") email: String? = null): List<AlbumCategoryCountDto>

    @GET("albums/{albumId}/{email}")
    suspend fun getUserAlbum(@Path("albumId") albumId: String? = null, @Path("email") email: String? = null): Album

    @GET("albums/{email}")
    suspend fun getUserAlbums(@Path("email") email: String? = null): List<Album>

    @GET("albums/category/{email}")
    suspend fun getUserAlbumsCategory(@Path("email") email: String? = null, @Query("categoryId") categoryId: String? = null): List<Album>

    @PUT("albums/{albumId}/{email}")
    suspend fun updateUserCardsForAlbum(@Path("albumId") albumId: String? = null, @Path("email") email: String? = null, @Body tradingCards: List<TradingCard>): Album

}