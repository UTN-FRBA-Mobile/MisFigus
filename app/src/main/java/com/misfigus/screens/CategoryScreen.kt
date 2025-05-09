package com.misfigus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misfigus.models.AlbumCategoryEnum

@Composable
fun CategoryScreen(navHostController: NavHostController){
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Categorias", style = MaterialTheme.typography.titleLarge)

        AlbumCategoryEnum.values().forEach { categoria ->
            Button(
                onClick = {
                    navHostController.navigate("$categoria")
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(categoria.toString(), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}