package com.misfigus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.misfigus.models.Album
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.CardColor
import com.misfigus.ui.theme.EditColor

@Composable
fun AlbumItem(album: Album, onClick: () -> Unit = {}){
    val total = album.tradingCards.size //TODO: Habria que ver si es correcto
    val obtained = album.tradingCards.count { it.obtained }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Background
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = album.albumId, style = MaterialTheme.typography.titleLarge)


                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "$obtained/$total",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (obtained == total) "Completado" else "Incompleto",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (obtained == total) EditColor else CardColor
                    )
                }

            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver álbum",
                tint = EditColor
            )

        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}