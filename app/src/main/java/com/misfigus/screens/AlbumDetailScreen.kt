package com.misfigus.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.components.TradingCardItem
import com.misfigus.models.Album
import com.misfigus.navigation.BackButton
import com.misfigus.ui.theme.Montserrat

@Composable
fun AlbumDetailScreen(navHostController: NavHostController, album: Album) {

    Scaffold(
        topBar = {BackButton(navHostController, "Albumes - ${album.category}")}
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F5FD))
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(
                text = album.albumId,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp),
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