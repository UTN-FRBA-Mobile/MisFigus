package com.misfigus.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misfigus.dto.AlbumCategoryCountDto
import com.misfigus.network.AlbumsApi
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

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getAlbumCountByCategory()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getAlbumCountByCategory() {
        viewModelScope.launch {
            albumsUiState = try {
                val listResult = AlbumsApi.retrofitService.getData()
                Log.d("API_RESPONSE", listResult.toString())
                AlbumsUiState.Success(listResult);
            } catch (e: IOException) {
                AlbumsUiState.Error
            }
        }
    }
}