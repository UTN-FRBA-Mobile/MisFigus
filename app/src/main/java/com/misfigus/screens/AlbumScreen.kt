package com.misfigus.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.misfigus.components.FiguritaItem
import com.misfigus.models.TradingCard

@Composable
fun AlbumScreen() {
    val tradingCards = remember {
        listOf(
            TradingCard(1, "albumA", obtained = true, 3),
            TradingCard(2, "albumA", obtained = false, repeatedQuantity = 0),
            TradingCard(3, "albumA", obtained = true, repeatedQuantity = 0)
        )
    }

    Text("Albumes", modifier = Modifier.padding(32.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(16.dp)
    ) {
        items(tradingCards) { figu ->
            FiguritaItem(tradingCard = figu)
        }
    }
}
