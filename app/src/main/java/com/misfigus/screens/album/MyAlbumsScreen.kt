package com.misfigus.screens.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.models.Album
import com.misfigus.screens.album.AlbumsUiState
import com.misfigus.screens.album.CategoriesUiState
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.Purple


@Composable
fun MyAlbums(navHostController: NavHostController, categoriesUiState: CategoriesUiState, albumsUiState: AlbumsUiState) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text("My albums", style = MaterialTheme.typography.titleLarge, color = Grey)
        MyCollectionStats(albumsUiState)
        CategoryScreen(navHostController, categoriesUiState)
    }
}

@Composable
fun MyCollectionStats(albumsUiState: AlbumsUiState) {
    when (albumsUiState) {
        is AlbumsUiState.Loading -> {
            Text(text = "Api call loading... [GET COUNT BY CATEGORIES]")
        }

        is AlbumsUiState.Error -> {
            Text(text = "Api call error [GET COUNT BY CATEGORIES]")
        }

        is AlbumsUiState.Success -> {

            val albums = albumsUiState.albums
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF7F9FE))
                    .padding(16.dp)
            ) {
                Text(
                    "Mi progreso de colección",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Barras horizontales
                albums.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        // Número de orden
                        Text("${index + 1}", modifier = Modifier.width(20.dp))

                        // Barra de fondo
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.LightGray)
                        ) {
                            // Progreso
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    //.fillMaxWidth(item.percentage)
                                    .clip(RoundedCornerShape(6.dp))
                                //.background(item.color)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Porcentaje
                        //Text("${(item.percentage * 100).toInt()}%", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detalle debajo del gráfico
                albums.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${index + 1}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(20.dp)
                        )
                        Text(
                            item.name,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp
                        )
                        Text(
                            "text",
                            //"${(item.percentage * 100).format(1)}%",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ver más ↓",
                    color = Color(0xFF5118AC),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            fun Float.format(decimals: Int): String = "%.${decimals}f".format(this)

        }

    }
}

@Composable
fun CategoryScreen(navHostController: NavHostController, categoriesUiState: CategoriesUiState){
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text("Categories", style = MaterialTheme.typography.titleMedium, color = Grey)
        Spacer(modifier = Modifier.padding(10.dp))

        when (categoriesUiState) {
            is CategoriesUiState.Loading -> {
                Text(text = "Api call loading... [GET COUNT BY CATEGORIES]")
            }

            is CategoriesUiState.Error -> {
                Text(text = "Api call error [GET COUNT BY CATEGORIES]")
            }
            is CategoriesUiState.Success -> {
                val albums = categoriesUiState.albumCountByCategory

                albums.forEach { item ->

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Background
                        )
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(
                                    painter = painterResource(id = item.category.icon),
                                    contentDescription = "My Icon",
                                    modifier = Modifier
                                        .size(40.dp),
                                )
                                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                    Text(item.category.name.lowercase().capitalize(), style = MaterialTheme.typography.bodyLarge)
                                    Text(item.count.toString() + if (item.count > 1) " álbumes" else " álbum"
                                        , style = MaterialTheme.typography.bodyLarge, color = Grey)

                                }
                            }
                            IconButton(onClick = {navHostController.navigate("category/" + item.category.description)}) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Ver categoria",
                                    tint = Purple
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}
