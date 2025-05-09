package com.misfigus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misfigus.components.AlbumItem
import com.misfigus.navigation.BackButton
import com.misfigus.navigation.mockedAlbums

@Composable
fun AlbumsScreen(navHostController: NavHostController, category: String) {

    val albums = mockedAlbums().filter {  category.equals(it.category.toString()) }
    Scaffold(
        topBar = { BackButton(navHostController, "Categorias") }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text = "Albumes - ${category}",
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.titleLarge
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)
            ) {

                items(albums) { abm ->
                    AlbumItem(
                        album = abm,
                        onClick = { navHostController.navigate("details/${abm.albumId}") })
                }
            }
        }
    }
}
