package com.misfigus.screens.trades.requests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.mock.mockTraderSofia
import com.misfigus.navigation.BackButton

@Composable
fun TradeRequestDetailScreen(requestId: String, navController: NavHostController) {
    Scaffold(
        topBar = { BackButton(navController, title = "Gestionar una solicitud de canje") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { /* TODO: Rechazar */ }) {
                    Text("Rechazar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* TODO: Aceptar */ }) {
                    Text("Aceptar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    //Text("Canjeá con ${mockTraderSofia.fullName}", fontSize = 18.sp, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        /*AsyncImage(
                            model = mockTraderSofia.profileImageUrl,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(50))
                        )*/

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(mockTraderSofia.location, color = Color.DarkGray)
                            Text(
                                if (mockTraderSofia.ships) "Hace envíos" else "No hace envíos",
                                color = Color.DarkGray
                            )
                            Text(
                                mockTraderSofia.reputation,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}