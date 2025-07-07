package com.misfigus.screens.album

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.models.Album
import com.misfigus.models.trades.Sticker
import com.misfigus.models.trades.TradingCard
import com.misfigus.network.AlbumApi
import com.misfigus.network.AuthApi
import kotlinx.coroutines.launch

sealed interface CategoriesUiState {
    data class Success(val albumCountByCategory: List<AlbumCategoryCountDto>) : CategoriesUiState
    object Error : CategoriesUiState
    object Loading : CategoriesUiState
}

sealed interface AlbumsUserCategoryUiState {
    data class Success(val albumsUserCategory: List<Album>) : AlbumsUserCategoryUiState
    object Error : AlbumsUserCategoryUiState
    object Loading : AlbumsUserCategoryUiState
}

sealed interface AlbumsUserUiState {
    data class Success(val albums: List<Album>) : AlbumsUserUiState
    object Error : AlbumsUserUiState
    object Loading : AlbumsUserUiState
}

sealed interface AlbumUserUiState {
    data class Success(val album: Album) : AlbumUserUiState
    object Error : AlbumUserUiState
    object Loading : AlbumUserUiState
}

sealed interface UpdateAlbumUiState {
    data class Success(val album: Album) : UpdateAlbumUiState
    object Error : UpdateAlbumUiState
    object Loading : UpdateAlbumUiState
}


class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    var categoriesUiState: CategoriesUiState by mutableStateOf(CategoriesUiState.Loading)
        private set

    var albumsUserUiState: AlbumsUserUiState by mutableStateOf(AlbumsUserUiState.Loading)
        private set

    var albumUserUiState: AlbumUserUiState by mutableStateOf(AlbumUserUiState.Loading)
        private set

    var albumsUserCategoryUiState: AlbumsUserCategoryUiState by mutableStateOf(AlbumsUserCategoryUiState.Loading)
        private set
    var updateAlbumUiState: UpdateAlbumUiState by mutableStateOf(UpdateAlbumUiState.Loading)
        private set

    fun getAlbumCountByCategory() {
        viewModelScope.launch {
            categoriesUiState = try {
                val context = getApplication<Application>().applicationContext
                val currentUser = AuthApi.getService(context).getCurrentUser()
                val listResult = AlbumApi.getService(context).getAlbumCountByCategory(currentUser.email)
                Log.d("API_RESPONSE", listResult.toString())
                CategoriesUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes por categoria: ${e.message}", e)
                CategoriesUiState.Error
            }
        }
    }

    fun getUserAlbum(albumId: String) {
        viewModelScope.launch {
            albumUserUiState = try {
                val context = getApplication<Application>().applicationContext
                val currentUser = AuthApi.getService(context).getCurrentUser()
                val listResult = AlbumApi.getService(context).getUserAlbum(albumId, currentUser.email)
                Log.d("API_RESPONSE", listResult.toString())
                AlbumUserUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                AlbumUserUiState.Error
            }
        }
    }

    fun getUserAlbums() {
        viewModelScope.launch {
            albumsUserUiState = try {
                val context = getApplication<Application>().applicationContext
                val currentUser = AuthApi.getService(context).getCurrentUser()
                val listResult = AlbumApi.getService(context).getUserAlbums(currentUser.email)
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUserUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                AlbumsUserUiState.Error
            }
        }
    }

    fun getUserAlbumsCategory(categoryId: String) {
        viewModelScope.launch {
            albumsUserCategoryUiState = try {
                val context = getApplication<Application>().applicationContext
                val currentUser = AuthApi.getService(context).getCurrentUser()
                val listResult = AlbumApi.getService(context).getUserAlbumsCategory(currentUser.email, categoryId)
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUserCategoryUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener categoria de álbumes: ${e.message}", e)
                AlbumsUserCategoryUiState.Error
            }
        }
    }

    fun updateUserCards(album: Album, changes: Map<String, Int>) {
        viewModelScope.launch {
            updateAlbumUiState = try {
                val context = getApplication<Application>().applicationContext
                val currentUser = AuthApi.getService(context).getCurrentUser()
                val updates = changes.map { (cardId, qty) ->
                    val obtained = if(qty > 0) true else false
                    TradingCard(number = cardId.toInt(), albumId = album.albumId, repeatedQuantity = qty, obtained = obtained)
                }
                val listResult = AlbumApi.getService(context).updateUserCardsForAlbum(album.id.toString(), currentUser.email, updates)
                Log.d("API_RESPONSE", listResult.toString())
                albumUserUiState = AlbumUserUiState.Success(listResult)
                UpdateAlbumUiState.Success(listResult)


            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al guardar figuritas", e)
                UpdateAlbumUiState.Error
            }
        }
    }

    fun clearState() {
        albumsUserUiState = AlbumsUserUiState.Loading
        categoriesUiState = CategoriesUiState.Loading
        albumsUserCategoryUiState = AlbumsUserCategoryUiState.Loading
        albumUserUiState = AlbumUserUiState.Loading
        albumsUserCategoryUiState = AlbumsUserCategoryUiState.Loading
    }
}
