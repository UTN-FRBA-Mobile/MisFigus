package com.misfigus.screens.album

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class AlbumsViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var albumsUiState: AlbumsUiState by mutableStateOf(AlbumsUiState.Loading)
        private set

    init {
        getAlbumCountByCategory()
    }

    fun getAlbumCountByCategory() {
        viewModelScope.launch {
            albumsUiState = try {
                val listResult = AlbumApi.retrofitService.getAlbums()
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUiState.Success(listResult);
            } catch (e: IOException) {
                AlbumsUiState.Error
            }
        }
    }
}