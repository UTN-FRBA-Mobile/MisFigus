package com.misfigus.screens.trades.requests

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.dto.TradeRequestDto
import com.misfigus.models.trades.TradeRequestStatus
import com.misfigus.navigation.BackButton
import com.misfigus.network.TradeApi
import com.misfigus.screens.trades.ForTraderSection
import com.misfigus.screens.trades.ForYouSection
import com.misfigus.screens.trades.TradeViewModel
import com.misfigus.screens.trades.TraderBanner
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.Green
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Red
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun SummaryToMe(trade: TradeRequestDto) {
    val firstCardText = trade.from.username + " te \nofrece " + trade.toGive.size + "\nfiguritas"
    val secondCardText = "A cambio\nquiere " + trade.stickers.size + "\nfiguritas"
    Summary(firstCardText, secondCardText)
}

@Composable
fun SummaryFromMe(trade: TradeRequestDto) {
    val firstCardText = "Le pediste\n" + trade.stickers.size + " figuritas\na " + trade.to.username
    val secondCardText = "A cambio\nle ofrecés\n" + trade.toGive.size + " figuritas"
    Summary(firstCardText, secondCardText)
}

@Composable
fun SummaryCard(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 40.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = color,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Summary(firstCardText: String, secondCardText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryCard(
            text = firstCardText,
            color = Purple,
        )
        
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Flecha derecha",
                tint = Purple,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Flecha izquierda",
                tint = Red,
                modifier = Modifier.size(32.dp)
            )
        }

        SummaryCard(
            text = secondCardText,
            color = Red
        )
    }
}

@Composable
fun TradeStatusDisplay(status: TradeRequestStatus) {
    val (statusText, statusColor) = when (status) {
        TradeRequestStatus.PENDING -> "Pendiente" to Color.Gray
        TradeRequestStatus.ACCEPTED -> "Aceptada" to Color.Green
        TradeRequestStatus.REJECTED -> "Rechazada" to Red
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            enabled = false,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(statusText, color = statusColor, fontSize = 13.sp)
        }
    }
}

@Composable
fun AcceptOrRejectButton(trade: TradeRequestDto, tradeId: UUID) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showRejectDialog by remember { mutableStateOf(false) }
    var showAcceptDialog by remember { mutableStateOf(false) }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        try {
                            val api = TradeApi.getService(context)
                            api.rejectTradeRequest(tradeId)
                            showRejectDialog = false
                        } catch (e: Exception) {
                            println("ERROR al rechazar solicitud")
                            e.printStackTrace()
                        }
                    }
                }) {
                    Text("Confirmar", color = Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Rechazar canje") },
            text = { Text("¿Estás seguro de que querés rechazar esta solicitud de canje?") }
        )
    }

    if (showAcceptDialog) {
        AlertDialog(
            onDismissRequest = { showAcceptDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        try {
                            val api = TradeApi.getService(context)
                            api.acceptTradeRequest(trade)
                            showAcceptDialog = false
                        } catch (e: Exception) {
                            println("ERROR al aceptar solicitud")
                            e.printStackTrace()
                        }
                    }
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancelar", color = Grey)
                }
            },
            title = { Text("Aceptar canje") },
            text = { Text("¿Estás seguro de que querés aceptar esta solicitud de canje?") }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { showRejectDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Rechazar", color = Purple, fontSize = 13.sp)
        }
        Button(
            onClick = { showAcceptDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Aceptar", color = Color.White, fontSize = 13.sp)
        }
    }
}

@Composable
fun AcceptOrReject(navHostController: NavHostController, tradeViewModel: TradeViewModel, sessionViewModel: SessionViewModel) {
    val trade = tradeViewModel.selectedTradeRequest.value
    Log.d("AcceptOrReject", "the trade is: $trade")

    val selectedFromYou = remember { mutableStateListOf<Int>() }
    val selectedToTrade = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = { BackButton(navHostController, "Canje") }
    ) { innerPadding ->
        if (trade != null) {
            val currentUserEmail = sessionViewModel.user?.email
            val isRequestSender = trade.from.email.equals(currentUserEmail)
            val otherUser = if (isRequestSender) trade.to else trade.from
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Solo mostrar botones si YO recibí la solicitud
                if (!isRequestSender && trade.status.equals(TradeRequestStatus.PENDING)) {
                    AcceptOrRejectButton(trade = trade, tradeId = UUID.fromString(trade.id))
                    Spacer(modifier = Modifier.height(20.dp))
                } else {
                    TradeStatusDisplay(trade.status)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                
                // Mostrar banner del "otro" usuario (quien no soy yo)
                TraderBanner(from = otherUser)
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Resumen del canje",
                    style = MaterialTheme.typography.titleMedium,
                    color = Grey,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 20.dp, end = 16.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                
                if (isRequestSender) {
                    // Yo envié la solicitud
                    SummaryFromMe(trade)
                } else {
                    // Yo recibí la solicitud
                    SummaryToMe(trade)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Detalle del canje",
                    style = MaterialTheme.typography.titleMedium,
                    color = Grey,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 20.dp, end = 16.dp)
                        .fillMaxWidth()
                )
                
                if (isRequestSender) {
                    // Yo envié la solicitud
                    ForYouSection(
                        albumName = trade.albumName,
                        traderName = otherUser.username,
                        stickers = trade.stickers, // Lo que YO recibo (lo que pedí)
                        selectedStickers = selectedFromYou,
                        onStickerClick = { }
                    )
                    ForTraderSection(
                        albumName = trade.albumName,
                        traderName = otherUser.username,
                        stickers = trade.toGive, // Lo que YO doy (lo que ofrezco)
                        selectedStickers = selectedToTrade,
                        onStickerClick = { }
                    )
                } else {
                    // Yo recibí la solicitud
                    ForYouSection(
                        albumName = trade.albumName,
                        traderName = otherUser.username,
                        stickers = trade.toGive, // Lo que YO recibo (lo que me ofrecen)
                        selectedStickers = selectedFromYou,
                        onStickerClick = { }
                    )
                    ForTraderSection(
                        albumName = trade.albumName,
                        traderName = otherUser.username,
                        stickers = trade.stickers, // Lo que YO doy (lo que me piden)
                        selectedStickers = selectedToTrade,
                        onStickerClick = { }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No hay información de canje disponible",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
