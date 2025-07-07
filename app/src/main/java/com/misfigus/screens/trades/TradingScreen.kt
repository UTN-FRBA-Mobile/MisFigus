package com.misfigus.screens.trades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.misfigus.components.ProfileImage
import com.misfigus.dto.PossibleTradeDto
import com.misfigus.network.TokenProvider
import com.misfigus.network.TradeApi
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.Red
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Grey
import createImageLoaderWithToken

@Composable
fun TradingBanner(totalTrades: Int, onNavigateToSolicitudes: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Canje",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(start = 16.dp).weight(1f)
                )
                Button(
                    onClick = onNavigateToSolicitudes,
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text("Solicitudes", color = Color.White, fontSize = 13.sp)
                }
            }
            Row(
                modifier = Modifier.padding(start = 16.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Error, contentDescription = null, tint = Red)
                Spacer(modifier = Modifier.width(4.dp))
                Text("¡Tenés $totalTrades canjes en tu zona!", fontSize = 13.sp, color = Red)
            }
        }
    }
}

@Composable
fun TraderCard(
    trade: PossibleTradeDto,
    navHostController: NavHostController,
    tradeViewModel: TradeViewModel
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                imageUrl = trade.from.profileImageUrl,
                size = 70.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trade.from.fullName,
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = trade.albumName, fontSize = 13.sp, color = Red)
                Text(
                    text = "Tiene ${trade.stickers.size} figuritas que te faltan",
                    fontSize = 13.sp,
                    color = Grey
                )
            }
            IconButton(onClick = {
                tradeViewModel.selectedTrade.value = trade
                navHostController.navigate("trader")
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Canjear", tint = Purple)
            }
        }
    }
}

@Composable
fun IntercambioScreen(
    navHostController: NavHostController,
    sessionViewModel: SessionViewModel,
    tradeViewModel: TradeViewModel
) {
    val context = LocalContext.current
    val token = TokenProvider.token
    val imageLoader = remember(token) {
        token?.let { createImageLoaderWithToken(context, it) }
    }

    var possibleTrades by remember { mutableStateOf(emptyList<PossibleTradeDto>()) }

    LaunchedEffect(Unit) {
        try {
            val api = TradeApi.getService(context)
            possibleTrades = api.getPossibleTrades()
        } catch (e: Exception) {
            println("ERROR al cargar solicitudes")
            e.printStackTrace()
        }
    }

    LazyColumn {
        item {
            TradingBanner(
                totalTrades = possibleTrades.size,
                onNavigateToSolicitudes = { navHostController.navigate("trade_requests") }
            )
        }
        items(possibleTrades) { trade ->
            TraderCard(
                trade = trade,
                navHostController = navHostController,
                tradeViewModel = tradeViewModel
            )
        }
    }
}
