package com.misfigus.screens.albums

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.screens.album.AlbumsUiState
import com.misfigus.screens.album.CategoriesUiState
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.Purple
import java.util.Locale

val progressColors = listOf(
    Color(0xFF6A1B9A), // púrpura
    Color(0xFF00BFA5), // verde
    Color(0xFFEC407A), // rosa
    Color(0xFFD32F2F), // rojo
)

@Composable
fun MyAlbums(navHostController: NavHostController, categoriesUiState: CategoriesUiState, albumsUiState: AlbumsUiState) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Mis Álbumes", style = MaterialTheme.typography.titleLarge, color = Grey)
        MyCollectionStats(albumsUiState)
        CategoryScreen(navHostController, categoriesUiState)
    }
}

@SuppressLint("DefaultLocale")
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

                albums.forEachIndexed { index, item ->
                    val barColor = progressColors[index % progressColors.size]
                    var percentage: Float = ((item.tradingCards.size.toFloat()/item.totalCards.toFloat()) * 100)
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

                albums.forEachIndexed { index, item ->
                    val percentage = (item.tradingCards.size.toFloat() / item.totalCards.toFloat()) * 100f
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

                Text(
                    text = "ver más ↓",
                    color = Purple,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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
