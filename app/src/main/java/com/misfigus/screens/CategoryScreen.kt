package com.misfigus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misfigus.models.AlbumCategoryEnum
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.EditColor

@Composable
fun CategoryScreen(navHostController: NavHostController){
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Categorías", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(10.dp))
        AlbumCategoryEnum.entries.forEach { category ->

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Background
                )
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = category.icon),
                            contentDescription = "My Icon",
                            modifier = Modifier
                                .size(40.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(category.name, style = MaterialTheme.typography.bodyLarge)
                    }
                    IconButton(onClick = {navHostController.navigate("$category")}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Ver categoria",
                            tint = EditColor
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
    }
}
