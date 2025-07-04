package com.misfigus.screens.trades.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.dto.mappings.TradeRequestMapper
import com.misfigus.models.trades.TradeRequest
import com.misfigus.models.trades.TradeRequestStatus
import com.misfigus.navigation.BackButton
import com.misfigus.network.TradeApi
import com.misfigus.screens.album.NewTag
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.LightPurple

@Composable
fun TradeRequestsScreen(
    navHostController: NavHostController,
    sessionViewModel: SessionViewModel
) {
    var selectedTab by remember { mutableStateOf("Recibidas") }
    val user by remember { derivedStateOf { sessionViewModel.user } }
    var allRequests by remember { mutableStateOf<List<TradeRequest>>(emptyList()) }
    val currentUserEmail = user?.email ?: ""
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val api = TradeApi.getService(context)
            val fetched = api.getAllTradeRequests()
            fetched.forEach { println(it) }
            allRequests = fetched.map(TradeRequestMapper::fromDto)
        } catch (e: Exception) {
            println("ERROR al cargar solicitudes")
            e.printStackTrace()
        }
    }

    val filteredRequests = when (selectedTab) {
        "Recibidas" -> allRequests.filter { it.toUserEmail == currentUserEmail }
        "Enviadas" -> allRequests.filter { it.fromUserEmail == currentUserEmail }
        "Todas" -> allRequests.filter { it.toUserEmail == currentUserEmail || it.fromUserEmail == currentUserEmail}
        else -> emptyList()
    }

    Scaffold(
        topBar = { BackButton(navHostController, title = "Detalle de solicitud") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text("Solicitudes", fontSize = 28.sp, modifier = Modifier.padding(bottom = 16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LightPurple, shape = RoundedCornerShape(12.dp))
                    .height(36.dp)
                    .clip(RoundedCornerShape(16.dp)),
                horizontalArrangement = Arrangement.Center
            ) {
                val tabs = listOf("Recibidas", "Enviadas", "Todas")

                tabs.forEachIndexed { index, tab ->
                    NewTag(
                        text = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        modifier = Modifier.weight(1f)
                    )

                    if (index != tabs.lastIndex) {
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(1.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredRequests.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay solicitudes ${selectedTab.lowercase()}.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(filteredRequests) { request ->
                        val subtitle = when (selectedTab) {
                            "Enviadas" -> when (request.status) {
                                TradeRequestStatus.ACCEPTED -> "Aceptó mi solicitud de canje"
                                TradeRequestStatus.REJECTED -> "Rechazó mi solicitud de canje"
                                TradeRequestStatus.PENDING -> "La solicitud enviada está pendiente"
                            }
                            "Recibidas" -> when (request.status) {
                                TradeRequestStatus.ACCEPTED -> "Aceptaste la solicitud de canje"
                                TradeRequestStatus.REJECTED -> "Rechazaste la solicitud de canje"
                                TradeRequestStatus.PENDING -> "Tienes una solicitud pendiente"
                            }
                            "Todas" -> {
                                val isSender = request.fromUserEmail == currentUserEmail
                                when (request.status) {
                                    TradeRequestStatus.ACCEPTED ->
                                        if (isSender) "Aceptó mi solicitud de canje" else "Aceptaste la solicitud de canje"
                                    TradeRequestStatus.REJECTED ->
                                        if (isSender) "Rechazó mi solicitud de canje" else "Rechazaste la solicitud de canje"
                                    TradeRequestStatus.PENDING ->
                                        if (isSender) "La solicitud enviada está pendiente" else "Tienes una solicitud pendiente"
                                }
                            }
                            else -> ""
                        }

                        TradeRequestCard(
                            request = request,
                            subtitle = subtitle,
                            isPendingAndUnseen = request.status == TradeRequestStatus.PENDING && !request.seen,
                            onClick = {
                                request.seen = true
                                navHostController.navigate("trade_request_detail/${request.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
