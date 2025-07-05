package com.misfigus.screens.trades

import android.util.Log
import com.example.misfigus.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.dto.PossibleTradeDto
import com.misfigus.network.TradeApi
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.Red
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Grey
import getUserProfilePictureId

@Composable
fun TradingBanner(totalTrades: Int, onNavigateToSolicitudes: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
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
fun TraderCard(trade: PossibleTradeDto, navHostController: NavHostController, tradeViewModel: TradeViewModel) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = getUserProfilePictureId(trade.from.username)),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trade.from.fullName,
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Text(
                    text = trade.albumName,
                    fontSize = 13.sp,
                    color = Red,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Text(
                    text = "Tiene ${trade.stickers.size} figuritas que te faltan",
                    fontSize = 13.sp,
                    color = Grey
                )
            }
            IconButton(onClick = {
                tradeViewModel.selectedTrade.value = trade
                navHostController.navigate("trader/{1}")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Canjear",
                    tint = Purple
                )
            }
        }
    }

}

@Composable
fun IntercambioScreen(navHostController: NavHostController, sessionViewModel: SessionViewModel, tradeViewModel: TradeViewModel) {
    var possibleTrades by remember { mutableStateOf<List<PossibleTradeDto>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val api = TradeApi.getService(context)
            val fetched = api.getPossibleTrades()
            possibleTrades = fetched
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