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
import com.misfigus.network.AlbumApi
import kotlinx.coroutines.launch

sealed interface CategoriesUiState {
    data class Success(val albumCountByCategory: List<AlbumCategoryCountDto>) : CategoriesUiState
    object Error : CategoriesUiState
    object Loading : CategoriesUiState
}

sealed interface AlbumsUiState {
    data class Success(val albumsCategory: List<Album>) : AlbumsUiState
    object Error : AlbumsUiState
    object Loading : AlbumsUiState
}

sealed interface AlbumUiState {
    data class Success(val albumCategory: Album) : AlbumUiState
    object Error : AlbumUiState
    object Loading : AlbumUiState
}

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    var categoriesUiState: CategoriesUiState by mutableStateOf(CategoriesUiState.Loading)
        private set

    var albumsUiState: AlbumsUiState by mutableStateOf(AlbumsUiState.Loading)
        private set

    var albumUiState: AlbumUiState by mutableStateOf(AlbumUiState.Loading)
        private set

    init {
        getAlbumCountByCategory()
    }

    fun getAlbumCountByCategory() {
        viewModelScope.launch {
            categoriesUiState = try {
                val context = getApplication<Application>().applicationContext
                val listResult = AlbumApi.getService(context).getAlbumCountByCategory()
                Log.d("API_RESPONSE", listResult.toString())
                CategoriesUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                CategoriesUiState.Error
            }
        }
    }

    fun getAlbumsCategory(categoryId: String) {
        viewModelScope.launch {
            albumsUiState = try {
                val context = getApplication<Application>().applicationContext
                val listResult = AlbumApi.getService(context).getAlbumsCategory(categoryId)
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                AlbumsUiState.Error
            }
        }
    }

    fun getAlbum(albumId: String) {
        viewModelScope.launch {
            albumUiState = try {
                val context = getApplication<Application>().applicationContext
                val listResult = AlbumApi.getService(context).getAlbum(albumId)
                Log.d("API_RESPONSE", listResult.toString())
                AlbumUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                AlbumUiState.Error
            }
        }
    }
}
