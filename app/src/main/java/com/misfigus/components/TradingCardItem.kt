package com.misfigus.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.misfigus.models.trades.TradingCard
import com.misfigus.ui.theme.Purple
import com.misfigus.ui.theme.Red


@Composable
fun TradingCardItem(tradingCard: TradingCard, currentQuantity: Int, isEditing: Boolean, onClick: () -> Unit = {}, onAdd: () -> Unit, onRemove: () -> Unit,){
    Box(modifier = Modifier.padding(2.dp)) {
        val obtained = tradingCard.obtained
        val repeatedCount = if (obtained) currentQuantity - 1 else currentQuantity
        Card(
            modifier = Modifier.padding(8.dp).size(width = 100.dp, height = 150.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (obtained) Red else Color.White
            ),
            border = BorderStroke(width = 2.dp, Red)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "  " + tradingCard.number.toString() + "  ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = if (obtained) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (obtained) Color.White else Red
                )
            }
        }
        if (repeatedCount > 0) {
            Box(
                modifier = Modifier
                    .absoluteOffset(x = 85.dp, y = (-2).dp)
                    .size(30.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(width = 2.dp, Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if(obtained)(currentQuantity - 1).toString() else currentQuantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Red
                )
            }

            if(isEditing){
                Box(
                    modifier = Modifier
                        .absoluteOffset(x = 0.dp, y = (-2).dp)
                        .size(30.dp)
                        .background(Purple, shape = CircleShape)
                        .border(width = 2.dp, Purple, CircleShape)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Delete",
                        tint = Color.White
                    )

                }
            }

        }
        if(isEditing){
            Box(
                modifier = Modifier
                    .absoluteOffset(x = 85.dp, y = 135.dp)
                    .size(30.dp)
                    .background(Purple, shape = CircleShape)
                    .border(width = 2.dp, Purple, CircleShape)
                    .clickable { onAdd() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )

            }
        }

    }
}