package com.example.misfigus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.misfigus.navigation.AppNav

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
            // tu navegación o theme acá
            AppNav()
        }
    }

}