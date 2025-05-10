package com.misfigus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.misfigus.navigation.AppNavigation
import com.misfigus.ui.theme.MisFigusTheme

// Clase principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear usuario por defecto
        val pedro = User(
            email = "pedro@gmail.com",
            username = "pedro",
            password = "pedro123"
        )
        UserRepository.register(pedro)

        setContent {
            MisFigusTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}




// Preview de como se ve
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MisFigusTheme {
        val navController = rememberNavController()
                AppNavigation(navController)
    }
}
