package com.misfigus.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.misfigus.models.Album
import com.misfigus.ui.theme.AlbumCompleted
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Red

@Composable
fun AlbumItem(album: Album, onClick: () -> Unit = {}){
    val total = album.totalCards
    val obtained = album.tradingCards.count { it.obtained }

    Card(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Background
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            val context = LocalContext.current
            val resId = remember() {
                context.resources.getIdentifier(
                    album.cover.lowercase(),
                    "drawable",
                    context.packageName
                )
            }
            Image(  // TODO arreglar que la imagen no queda bien cuando se filtra
                painter = painterResource(id = resId),
                contentDescription = "My Album",
                modifier = Modifier
                    .width(50.dp)
                    .height(59.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(text = album.name, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "$obtained/$total",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (obtained == total) "Completado" else "Incompleto",
                        style = if (obtained == total) MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline) else MaterialTheme.typography.bodySmall,
                        color = if (obtained == total) AlbumCompleted else Red
                    )
                }

            }

            IconButton(onClick = {onClick()}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ver álbum",
                    tint = Purple,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

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
