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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.session.SessionViewModel
import com.misfigus.ui.theme.Red
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Grey

@Composable
fun TraddingBanner(onNavigateToSolicitudes: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                Text("Hay 15 personas de tu zona que quieren canjear", fontSize = 13.sp, color = Red)
            }
        }
    }
}

@Composable
fun TraderCard(navHostController: NavHostController) {
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
                painter = painterResource(id = R.drawable.matias),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Matías, Caballito",
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
//               Row(
//                verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Book,
//                        contentDescription = "Book Icon",
//                        tint = CardColor
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Fifa World Cup Qatar 2022",
//                        fontSize = 13.sp,
//                        color = CardColor
//                    )
//                }
                Text(
                    text = "Fifa World Cup Qatar 2022",
                    fontSize = 13.sp,
                    color = Red,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Text(
                    text = "Tiene 6 figuritas que te faltan",
                    fontSize = 13.sp,
                    color = Grey
                )
            }
            IconButton(onClick = {navHostController.navigate("trader/{1}")}) {
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
fun IntercambioScreen(navHostController: NavHostController, sessionViewModel: SessionViewModel) {
    val currentUser = sessionViewModel.user
    Log.d("", "current user iis")
    Log.d("TAG", "IntercambioScreen: $currentUser")
    Column {
        TraddingBanner {
            navHostController.navigate("trade_requests")
        }
        TraderCard(navHostController)
    }
}