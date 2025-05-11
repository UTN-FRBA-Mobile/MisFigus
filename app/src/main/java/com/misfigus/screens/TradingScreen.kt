package com.misfigus.screens

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
import com.misfigus.ui.theme.CardColor
import com.misfigus.ui.theme.EditColor
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.White

@Composable
fun TraddingBanner() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column {
            Text(
                "Canje",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.padding(start = 16.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = "Error Icon",
                    tint = CardColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Hay 15 personas de tu zona que quieren canjear",
                    fontSize = 13.sp,
                    color = CardColor
                )
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
            containerColor = White
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
                    color = CardColor,
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
                    tint = EditColor
                )
            }
        }
    }
}

@Composable
fun IntercambioScreen(navHostController: NavHostController) {
    Column() {
        TraddingBanner()
        TraderCard(navHostController)
    }

}
