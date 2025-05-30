package com.misfigus.screens.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.misfigus.components.AlbumItem
import com.misfigus.models.AlbumCategoryEnum
import com.misfigus.navigation.BackButton
import com.misfigus.navigation.mockedAlbums
import com.misfigus.ui.theme.Purple

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar"
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
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun NewTag() {
    Row(
        modifier = Modifier
            .padding(end = 16.dp)
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
fun AlbumsFromCategory(navHostController: NavHostController, category: AlbumCategoryEnum) {
    var searchQuery by remember { mutableStateOf("") }

    val albums = mockedAlbums().filter {  category.description.equals(it.category.description) }

    Scaffold(
        topBar = { BackButton(navHostController, "My albums") }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
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
                val filteredAlbums = albums.filter { it.albumId.contains(searchQuery, ignoreCase = true) }
                items(filteredAlbums) { abm ->
                    AlbumItem(
                        album = abm,
                        onClick = { navHostController.navigate("details/${abm.albumId}") })
                }
            }
        }
    }
}
