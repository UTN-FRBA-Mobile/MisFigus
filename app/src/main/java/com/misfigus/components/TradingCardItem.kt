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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.misfigus.models.TradingCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import com.misfigus.ui.theme.EditColor
import com.misfigus.ui.theme.CardColor


@Composable
fun TradingCardItem(tradingCard: TradingCard, onClick: () -> Unit = {}){
    Box(modifier = Modifier.padding(2.dp)) {
        Card(
            modifier = Modifier.padding(8.dp).size(width = 100.dp, height = 150.dp)
                .clickable { onClick },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (tradingCard.obtained) CardColor else Color.White
            ),
            border = BorderStroke(width = 2.dp, CardColor)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = tradingCard.number.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (tradingCard.obtained) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (tradingCard.obtained) Color.White else CardColor
                )
            }
        }
        if (tradingCard.repeatedQuantity > 0) {
            Box(
                modifier = Modifier
                    .absoluteOffset(x = 85.dp, y = (-2).dp)
                    .size(30.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(width = 2.dp, CardColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tradingCard.repeatedQuantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = CardColor
                )
            }

            Box(
                modifier = Modifier
                    .absoluteOffset(x = 0.dp, y = (-2).dp)
                    .size(30.dp)
                    .background(EditColor, shape = CircleShape)
                    .border(width = 2.dp, EditColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
        Box(
            modifier = Modifier
                .absoluteOffset(x = 85.dp, y = 135.dp)
                .size(30.dp)
                .background(EditColor, shape = CircleShape)
                .border(width = 2.dp, EditColor, CircleShape),
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