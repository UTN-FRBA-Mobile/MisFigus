package com.misfigus.screens.album

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.network.AlbumApi
import com.misfigus.network.TokenProvider.token
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface AlbumsUiState {
    data class Success(val albumCountByCategory: List<AlbumCategoryCountDto>) : AlbumsUiState
    object Error : AlbumsUiState
    object Loading : AlbumsUiState
}

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    var albumsUiState: AlbumsUiState by mutableStateOf(AlbumsUiState.Loading)
        private set

    init {
        getAlbumCountByCategory()
    }

    fun getAlbumCountByCategory() {
        viewModelScope.launch {
            albumsUiState = try {
                val context = getApplication<Application>().applicationContext
                val listResult = AlbumApi.getService(context).getAlbums()
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUiState.Success(listResult)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener álbumes: ${e.message}", e)
                AlbumsUiState.Error
            }
        }
    }
}
