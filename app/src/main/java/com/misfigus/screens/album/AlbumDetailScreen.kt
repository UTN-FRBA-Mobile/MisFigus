package com.misfigus.screens.album

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.components.TradingCardItem
import com.misfigus.models.Album
import com.misfigus.navigation.BackButton
import com.misfigus.ui.theme.Purple

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(navHostController: NavHostController, initialAlbum: Album,  viewModel: AlbumsViewModel) {

    val albumUserUiState = viewModel.albumUserUiState

    var isEditing by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var modifiedCards by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    LaunchedEffect(Unit) {
        viewModel.getUserAlbum(initialAlbum.id.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Álbumes - ${initialAlbum.category.spanishDesc}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    BackButton(navHostController, "Albumes - ${initialAlbum.category.spanishDesc}")
                },
                actions = {
                    editButton(isEditing = isEditing){
                        if (isEditing) {
                            if (modifiedCards.isNotEmpty()) {
                                if (albumUserUiState is AlbumUserUiState.Success) {
                                    viewModel.updateUserCards(albumUserUiState.album, modifiedCards.toMap())
                                }
                                modifiedCards = emptyMap()
                            }
                        }
                        isEditing = !isEditing
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F5FD))
                .padding(horizontal = 15.dp)
                .padding(top = 100.dp)

        ) {
            when (albumUserUiState) {
                is AlbumUserUiState.Loading -> {
                    Text(text = "Api call loading... [GET ALBUM BY ID]")
                }

                is AlbumUserUiState.Error -> {
                    Text(text = "Api call error [GET ALBUM BY ID]")
                }

                is AlbumUserUiState.Success -> {
                    val album = albumUserUiState.album

                    Text(
                        text = album.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        val filteredCards = album.tradingCards.filter { it.number.toString()?.contains(searchQuery, ignoreCase = true) == true}
                        items(filteredCards) { tradeCard ->
                            val currentQuantity = modifiedCards[tradeCard.number.toString()] ?: tradeCard.repeatedQuantity

                            TradingCardItem(tradingCard = tradeCard, isEditing = isEditing, currentQuantity = currentQuantity,
                            onAdd = {
                                val current = modifiedCards[tradeCard.number.toString()] ?: tradeCard.repeatedQuantity
                                modifiedCards = modifiedCards.toMutableMap().apply {
                                    put(tradeCard.number.toString(), current + 1)
                                }
                            },
                            onRemove = {
                                val current = modifiedCards[tradeCard.number.toString()] ?: tradeCard.repeatedQuantity
                                if (current > 0) {
                                    modifiedCards = modifiedCards.toMutableMap().apply {
                                        put(tradeCard.number.toString(), current - 1)
                                    }
                                }
                            })
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun editButton(isEditing: Boolean, onToggleEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(end = 16.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Purple)
            .clickable { onToggleEdit() }
            .padding(horizontal = 12.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isEditing) "Guardar" else "Editar",
            fontSize = 15.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(onClick = {}) { // TODO add screen
            Icon(
                imageVector = if (!isEditing) Icons.Outlined.Edit else Icons.Outlined.Save,
                contentDescription = "Edit",
                tint = Color.White
            )
        }
    }
}