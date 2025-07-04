package com.misfigus.screens.trades

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.misfigus.dto.UserDto
import com.misfigus.navigation.BackButton
import com.misfigus.ui.theme.Red
import com.misfigus.ui.theme.Grey
import getUserProfilePictureId

var myStickers = listOf(
    mapOf("name" to "AR", "number" to "1"),
    mapOf("name" to "AR", "number" to "10")
)

var matiasStickers = listOf(
    mapOf("name" to "AR", "number" to "3"),
    mapOf("name" to "AR", "number" to "4")
)

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
fun TraderBanner(from : UserDto) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Canjeá con ${from.username}",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = getUserProfilePictureId(from.username)),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    TextWithIcon(
                        text = from.location,
                        textColor = Grey,
                        imageColor = Grey,
                        image = Icons.Outlined.LocationOn
                    )
                    val shipping = if (from.shipping) "Hace" else "No hace"
                    TextWithIcon(
                        text = "${shipping} envios",
                        textColor = Grey,
                        imageColor = Grey,
                        image = if (from.shipping) Icons.Outlined.LocalShipping else Icons.Outlined.Block
                    )
                    val reputation = from.reputation
                    val text = if (reputation == "good") "Buena" else "Mala"
                    TextWithIcon(
                        text = " ${text} reputación",
                        textColor = Grey,
                        imageColor = Grey,
                        image = if (reputation == "good") Icons.Outlined.ThumbUp else Icons.Outlined.ThumbDown
                    )
                }
            }
        }
    }
}

@Composable
fun Sticker(name: String, number: String) {
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(
                color = if (isClicked) Red else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(width = 1.dp, color = Red, shape = RoundedCornerShape(8.dp))
            .padding(6.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { isClicked = !isClicked },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = name,
                fontSize = 13.sp,
                color = if (isClicked) Color.White else Red,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = number,
                fontSize = 13.sp,
                color = if (isClicked) Color.White else Red,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
fun TradeSection(traderName: String, albumName: String, message: String, stickers: List<Integer> = emptyList()) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "PARA ${traderName.uppercase()}",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            TextWithIcon(
                text = albumName,
                textColor = Red,
                imageColor = Red,
                image = Icons.AutoMirrored.Outlined.MenuBook
            )
            Text(
                text = message,
                fontSize = 13.sp,
                color = Grey,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                stickers.forEach { sticker ->
                    Sticker(
                        //name = sticker["name"].toString(),
                        name = "XXX",
                        number = sticker.toString()
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}

@Composable
fun ForYouSection(albumName: String, traderName: String, stickers: List<Integer> = emptyList()) {
    TradeSection(
        traderName = "Vos",
        albumName = albumName,
        message = "${traderName} tiene ${stickers.size} figuritas que te faltan",
        stickers = stickers
    )
}

@Composable
fun ForTraderSection(albumName: String, traderName: String, stickers: List<Integer> = emptyList()) {
    TradeSection(
        traderName = traderName,
        albumName = albumName,
        message = "Tenés ${stickers.size} figuritas que ${traderName} necesita",
        stickers = stickers
    )
}

@Composable
fun MoreAlbums() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
fun TraderOptionsScreen(navHostController: NavHostController, id: String, tradeViewModel: TradeViewModel) {
    val trade = tradeViewModel.selectedTrade.value
    Log.d("TraderOptionsScreen", "the trade is: $trade")
    Scaffold(
        topBar = { BackButton(navHostController, "Canje") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            TraderBanner(from = trade!!.from)
            ForYouSection(
                albumName = trade.albumName,
                traderName = trade.from.username,
                stickers = trade.stickers
            )
            ForTraderSection(
                albumName = trade.albumName,
                traderName = trade.from.username,
                stickers = trade.toGive
            )
            MoreAlbums()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
