package com.misfigus.screens.trades.requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.misfigus.dto.TradeRequestDto
import com.misfigus.models.trades.TradeRequest
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
            Image(
                painter = painterResource(id = getUserProfilePictureId(request.from.username)),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentScale = ContentScale.Crop
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
                text = request.from.email.split("@")[0].replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = if (isPendingAndUnseen) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        Text("→", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
    }
}
