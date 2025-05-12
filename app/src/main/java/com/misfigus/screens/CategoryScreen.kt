package com.misfigus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.EditColor
import com.misfigus.ui.theme.Grey


@Composable
fun CategoryScreen(navHostController: NavHostController, albumsUiState: AlbumsUiState){
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Categorías", style = MaterialTheme.typography.titleMedium, color = Grey)
        Spacer(modifier = Modifier.padding(10.dp))

        when (albumsUiState) {
            is AlbumsUiState.Loading -> {
                Text(text = "Api call loading... [GET ALBUMS]")
            }

            is AlbumsUiState.Error -> {
                Text(text = "Api call error [GET ALBUMS]")
            }
            is AlbumsUiState.Success -> {
                val albums = albumsUiState.albumCountByCategory

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
                            IconButton(onClick = {navHostController.navigate(item.category.description)}) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Ver categoria",
                                    tint = EditColor
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
