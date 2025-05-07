package com.example.misfigus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.misfigus.AlbumScreen
import com.example.misfigus.LoginScreen
import com.example.misfigus.PresentationScreen
import com.example.misfigus.RegisterScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "presentation") {
        composable("presentation") {
            PresentationScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("albumes") {
            AlbumScreen(navController)
        }
    }
}
