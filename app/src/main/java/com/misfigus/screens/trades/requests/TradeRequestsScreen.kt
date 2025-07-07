package com.misfigus.screens.trades.requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.components.ProfileImage
import com.misfigus.dto.TradeRequestDto
import com.misfigus.models.trades.TradeRequestStatus
import com.misfigus.navigation.BackButton
import com.misfigus.network.TradeApi
import com.misfigus.screens.trades.TradeViewModel
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.LightPurple
import com.misfigus.ui.theme.Purple
import getUserProfilePictureId

@Composable
fun TradeRequestCard(
    request: TradeRequestDto,
    subtitle: String,
    isPendingAndUnseen: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {

            ProfileImage(
                imageUrl = request.from.profileImageUrl,
                size = 40.dp
            )

            if (isPendingAndUnseen) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 14.sp
            )
        }

        Text("→", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
    }
}

@Composable
fun NewTag(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Purple else Color.Transparent
    val contentColor = if (isSelected) Color.White else Purple

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TradeRequestsScreen(
    navHostController: NavHostController,
    sessionViewModel: SessionViewModel,
    tradeViewModel: TradeViewModel
) {
    var selectedTab by remember { mutableStateOf("Recibidas") }
    val user by remember { derivedStateOf { sessionViewModel.user } }
    var allRequests by remember { mutableStateOf<List<TradeRequestDto>>(emptyList()) }
    val currentUserEmail = user?.email ?: ""
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val api = TradeApi.getService(context)
            allRequests = api.getAllTradeRequests()
        } catch (e: Exception) {
            println("ERROR al cargar solicitudes")
            e.printStackTrace()
        }
    }

    val filteredRequests = when (selectedTab) {
        "Recibidas" -> allRequests.filter { it.to.email == currentUserEmail }
        "Enviadas" -> allRequests.filter { it.from.email == currentUserEmail }
        "Todas" -> allRequests.filter { it.to.email == currentUserEmail || it.from.email == currentUserEmail}
        else -> emptyList()
    }

    Scaffold(
        topBar = { BackButton(navHostController, title = "Canje") }
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
                    Text("No hay solicitudes")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(filteredRequests) { request ->
                        val (profilePictureId, userName, subtitle) = when (selectedTab) {
                            "Enviadas" -> {
                                val targetUser = request.to
                                when (request.status) {
                                    TradeRequestStatus.ACCEPTED -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        "Aceptó mi solicitud de canje"
                                    )
                                    TradeRequestStatus.REJECTED -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        "Rechazó mi solicitud de canje"
                                    )
                                    TradeRequestStatus.PENDING -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        "La solicitud enviada está pendiente"
                                    )
                                }
                            }
                            "Recibidas" -> {
                                val sourceUser = request.from
                                when (request.status) {
                                    TradeRequestStatus.ACCEPTED -> Triple(
                                        getUserProfilePictureId(sourceUser.username),
                                        sourceUser.fullName,
                                        "Aceptaste la solicitud de canje"
                                    )
                                    TradeRequestStatus.REJECTED -> Triple(
                                        getUserProfilePictureId(sourceUser.username),
                                        sourceUser.fullName,
                                        "Rechazaste la solicitud de canje"
                                    )
                                    TradeRequestStatus.PENDING -> Triple(
                                        getUserProfilePictureId(sourceUser.username),
                                        sourceUser.fullName,
                                        "Tenés una solicitud pendiente"
                                    )
                                }
                            }
                            "Todas" -> {
                                val isSender = request.from.email == currentUserEmail
                                val targetUser = if (isSender) request.to else request.from
                                when (request.status) {
                                    TradeRequestStatus.ACCEPTED -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        if (isSender) "Aceptó mi solicitud de canje" else "Aceptaste la solicitud de canje"
                                    )
                                    TradeRequestStatus.REJECTED -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        if (isSender) "Rechazó mi solicitud de canje" else "Rechazaste la solicitud de canje"
                                    )
                                    TradeRequestStatus.PENDING -> Triple(
                                        getUserProfilePictureId(targetUser.username),
                                        targetUser.fullName,
                                        if (isSender) "La solicitud enviada está pendiente" else "Tenés una solicitud pendiente"
                                    )
                                }
                            }
                            else -> Triple(
                                getUserProfilePictureId("unknown"),
                                "Usuario desconocido",
                                ""
                            )
                        }

                        TradeRequestCard(
                            request = request,
                            profilePictureId = profilePictureId,
                            userName = userName,
                            subtitle = subtitle,
                            onClick = {
                                tradeViewModel.selectedTradeRequest.value = request
                                navHostController.navigate("trade_request_detail")
                            }
                        )
                    }
                }
            }
        }
    }
}
