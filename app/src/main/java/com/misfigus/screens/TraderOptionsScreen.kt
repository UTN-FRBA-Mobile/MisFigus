package com.misfigus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misfigus.R
import com.misfigus.navigation.BackButton
import com.misfigus.ui.theme.CardColor
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.White

@Composable
fun TextWithIcon(text: String, textColor: Color, imageColor: Color, image: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = image,
            contentDescription = "Icon",
            tint = imageColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TraderBanner() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Canjeá con Matías",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.matias),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    TextWithIcon(
                        text = "Caballito, Ciudad de Buenos Aires",
                        textColor = Grey,
                        imageColor = Grey,
                        image = Icons.Outlined.LocationOn
                    )
                    TextWithIcon(
                        text = "Hace envios",
                        textColor = Grey,
                        imageColor = Grey,
                        image = Icons.Outlined.LocalShipping
                    )
                    TextWithIcon(
                        text = "Buena reputación",
                        textColor = Grey,
                        imageColor = Grey,
                        image = Icons.Outlined.ThumbUp
                    )
                }
            }
        }
    }
}

@Composable
fun Sticker(name: String,number: String) {
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = CardColor, shape = RoundedCornerShape(8.dp))
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = name,
                fontSize = 13.sp,
                color = CardColor,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = number,
                fontSize = 13.sp,
                color = CardColor,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
fun ForYouSection() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "PARA VOS",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            TextWithIcon(
                text = "Fifa World Cup Qatar 2022",
                textColor = CardColor,
                imageColor = CardColor,
                image = Icons.AutoMirrored.Outlined.MenuBook
            )
            Text(
                text = "Matías tiene 13 figuritas que te faltan",
                fontSize = 13.sp,
                color = Grey,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Sticker(
                    name = "AR",
                    number = "3"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Sticker(
                    name = "AR",
                    number = "4"
                )
            }
        }
    }
}

@Composable
fun ForTraderSection() {

}

@Composable
fun MoreAlbums() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Otros álbumes",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = "Matías tiene figuritas de otros álbumes que te faltan, ¡mirá!",
                fontSize = 13.sp,
                color = Grey,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
fun TraderOptionsScreen(navHostController: NavHostController, id: String) {
//    Text("trader options. id $id")
    Scaffold(
        topBar = { BackButton(navHostController, "Canje") }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TraderBanner()
            ForYouSection()
//            ForTraderSection()
            MoreAlbums()
        }
    }
}
