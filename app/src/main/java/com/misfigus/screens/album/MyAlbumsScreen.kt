package com.misfigus.screens.albums

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.screens.album.AlbumsUserUiState
import com.misfigus.screens.album.CategoriesUiState
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.Purple
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.LaunchedEffect
import com.misfigus.models.Album
import com.misfigus.screens.album.AlbumsViewModel
import com.misfigus.session.SessionViewModel

val progressColors = listOf(
    Color(0xFF6A1B9A), // púrpura
    Color(0xFF00BFA5), // verde
    Color(0xFFEC407A), // rosa
    Color(0xFFD32F2F), // rojo
)

@Composable
fun MyAlbums(navHostController: NavHostController, albumsViewModel: AlbumsViewModel, sessionViewModel: SessionViewModel) {
    val user = sessionViewModel.user

    LaunchedEffect(user?.email) {
        if (user != null) {
            albumsViewModel.getUserAlbums()
            albumsViewModel.getAlbumCountByCategory()
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Mis Álbumes", style = MaterialTheme.typography.titleLarge, color = Grey)
        MyCollectionStats(albumsViewModel.albumsUserUiState)
        CategoryScreen(navHostController, albumsViewModel.categoriesUiState)
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MyCollectionStats(albumsUserUiState: AlbumsUserUiState) {
    var expanded by remember { mutableStateOf(false) }
    when (albumsUserUiState) {
        is AlbumsUserUiState.Loading -> {
            Text(text = "Api call loading... [GET COUNT BY CATEGORIES]")
        }

        is AlbumsUserUiState.Error -> {
            Text(text = "Api call error [GET COUNT BY CATEGORIES]")
        }

        is AlbumsUserUiState.Success -> {
            val albums = albumsUserUiState.albums

            val sortedAlbums = albums.sortedWith(
                compareByDescending<Album> { album ->
                    val obtained = album.tradingCards.count { it.obtained }
                    obtained.toFloat() / album.totalCards.toFloat()
                }.thenBy { it.name.lowercase() }
            )

            val visibleAlbums = if (expanded) sortedAlbums.take(4) else sortedAlbums.take(2)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.5f))


                    .border(
                        width = 1.dp,
                        color = Color(0x33000000),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    "Mi progreso de colección",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(20.dp))
                    repeat(5) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${it * 25}%",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFDDDDDD))
                        .padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                visibleAlbums.forEachIndexed { index, item ->
                    val barColor = progressColors[index % progressColors.size]
                    val obtainedCards = item.tradingCards.filter { card -> card.obtained }.size
                    var percentage: Float = ((obtainedCards.toFloat()/item.totalCards.toFloat()) * 100)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Text("${index + 1}", modifier = Modifier.width(20.dp), style = MaterialTheme.typography.bodySmall)

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(percentage / 100f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(barColor)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(String.format("%.2f%%", percentage), fontSize = 12.sp)
                    }
                }

                // Línea inferior horizontal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFDDDDDD))
                        .padding(start = 20.dp)
                )


                Spacer(modifier = Modifier.height(16.dp))

                visibleAlbums.forEachIndexed { index, item ->
                    val obtainedCards = item.tradingCards.filter { card -> card.obtained }.size
                    var percentage: Float = ((obtainedCards.toFloat()/item.totalCards.toFloat()) * 100)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(20.dp)
                        )
                        Text(
                            item.name,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(String.format("%.2f%%", percentage),  style = MaterialTheme.typography.bodySmall)

                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (albums.size > 2) {
                    Text(
                        text = if (expanded) "ver menos ↑" else "ver más ↓",
                        color = Purple,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { expanded = !expanded }
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryScreen(navHostController: NavHostController, categoriesUiState: CategoriesUiState){
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text("Categorías", style = MaterialTheme.typography.titleMedium, color = Grey)
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
                                    Text(item.category.spanishDesc, style = MaterialTheme.typography.bodyLarge)
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
