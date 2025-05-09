package com.misfigus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.misfigus.ui.theme.EditColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackButton(navController: NavHostController, title: String) {
    val canNavigateBack = navController.previousBackStackEntry != null

    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.labelSmall, color = EditColor) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Volver", tint = EditColor)
                }
            }
        }
    )
}