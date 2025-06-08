package com.misfigus.screens.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misfigus.components.TradingCardItem
import com.misfigus.models.Album
import com.misfigus.navigation.BackButton

@Composable
fun AlbumDetailScreen(navHostController: NavHostController, album: Album,  viewModel: AlbumsViewModel) {

    val albumUiState = viewModel.albumUiState

    LaunchedEffect(album) {
        viewModel.getAlbum(album.albumId)
    }

    Scaffold(
        topBar = {BackButton(navHostController, "Albumes - ${album.category.spanishDesc}")}
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F5FD))
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            when (albumUiState) {
                is AlbumUiState.Loading -> {
                    Text(text = "Api call loading... [GET ALBUM BY ID]")
                }

                is AlbumUiState.Error -> {
                    Text(text = "Api call error [GET ALBUM BY ID]")
                }

                is AlbumUiState.Success -> {
                    val album = albumUiState.album
                    Text(
                        text = album.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(album.tradingCards) { tradeCard ->
                            TradingCardItem(tradingCard = tradeCard)
                        }
                    }
                }
            }

        }
    }
}