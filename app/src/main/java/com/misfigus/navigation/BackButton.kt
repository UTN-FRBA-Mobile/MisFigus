package com.misfigus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.misfigus.ui.theme.Background
import com.misfigus.ui.theme.Grey
import com.misfigus.ui.theme.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackButton(navController: NavHostController, title: String) {
    val canNavigateBack = navController.previousBackStackEntry != null

    TopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Purple)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Volver", tint = Purple)
                }
            }
        },
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background
        )
    )
}