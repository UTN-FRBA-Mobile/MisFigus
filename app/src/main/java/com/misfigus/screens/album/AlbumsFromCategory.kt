package com.misfigus.screens.album

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.components.AlbumItem
import com.misfigus.models.AlbumCategoryEnum
import com.misfigus.navigation.BackButton
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Search


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            //.padding(horizontal = 2.dp) // margen interno entre borde y TextField
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = Search,
                unfocusedContainerColor = Search,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }

}

@Composable
fun NewTag() {
    Row(
        modifier = Modifier
            .padding(end = 16.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Purple)
            .padding(horizontal = 12.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nuevo",
            fontSize = 15.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(onClick = {}) { // TODO add screen
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
fun AlbumsFromCategory(navHostController: NavHostController, category: AlbumCategoryEnum, viewModel: AlbumsViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    val albumsUiState = viewModel.albumsUiState

    LaunchedEffect(category) {
        viewModel.getAlbumsCategory(category.description)
    }

    Scaffold(
        topBar = { BackButton(navHostController, "My albums") }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (albumsUiState) {
                is AlbumsUiState.Loading -> {
                    Text(text = "Api call loading... [GET ALBUMS BY CATEGORIES]")
                }

                is AlbumsUiState.Error -> {
                    Text(text = "Api call error [GET ALBUMS BY CATEGORIES]")
                }
                is AlbumsUiState.Success -> {
                    val albums = albumsUiState.albumsCategory
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            modifier = Modifier.weight(1f)
                        )
                        NewTag()
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)
                    ) {
                        val filteredAlbums = albums.filter { it.albumId?.contains(searchQuery, ignoreCase = true) == true}
                        items(filteredAlbums) { abm ->
                            AlbumItem(
                                album = abm,
                                onClick = { navHostController.navigate("details/${abm.albumId}") })
                        }
                    }

                }
            }

        }
    }
}
