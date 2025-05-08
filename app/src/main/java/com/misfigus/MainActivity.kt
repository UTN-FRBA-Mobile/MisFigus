package com.misfigus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.misfigus.R
import com.misfigus.screens.AlbumScreen
import com.misfigus.screens.IntercambioScreen
import com.misfigus.screens.KioscoScreen
import com.misfigus.screens.LoginScreen
import com.misfigus.ui.theme.MisFigusTheme

// Cada data class representa cada pestaña de la barra de navegacion
sealed class Screen(val route: String, val iconType: IconType) {
    data object Search : Screen("search", IconType.Vector(Icons.Default.Search))
    data object Album : Screen("album", IconType.Drawable(R.drawable.ic_menu_book))
    data object Exchange : Screen("exchange", IconType.Drawable(R.drawable.ic_swap))
    data object Profile : Screen("profile", IconType.Vector(Icons.Default.Person))
}

// Clase con los dos tipos de iconos de la barra de navegacion
sealed class IconType {
    // Ícono vectorial (Los que ya estan disponibles de android)
    data class Vector(val icon: androidx.compose.ui.graphics.vector.ImageVector) : IconType()
    // Ícono desde drawable (archivo XML/SVG en res/drawable)
    data class Drawable(val resId: Int) : IconType()
}

// Lista de todas las pantallas que aparecen en la barra de navegación inferior
val screens = listOf(
    Screen.Search,
    Screen.Album,
    Screen.Exchange,
    Screen.Profile
)

// Clase principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MisFigusTheme {
                MainScreen()
            }
        }
    }
}

// Función Composable con la lógica de navegación y la barra inferior
@Composable
fun MainScreen() {
    val navController = rememberNavController() // Controlador para manejar la navegacion
    val currentBackStack by navController.currentBackStackEntryAsState() // Historial de navegacion para conseguir la pantalla actual
    val currentRoute = currentBackStack?.destination?.route // Obtiene la pantalla actual

    // Crea la estructura visual principal, incluyendo la barra inferior
    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen -> // Recorre todas las pestañas
                    NavigationBarItem(
                        icon = {
                            when (val icon = screen.iconType) {
                                is IconType.Vector -> Icon(icon.icon, contentDescription = null)
                                is IconType.Drawable -> Icon(
                                    painter = painterResource(id = icon.resId),
                                    contentDescription = null
                                )
                            }
                        },
                        selected = currentRoute == screen.route, // Marca como seleccionada la pestaña actual
                        onClick = {
                            navController.navigate(screen.route) { // Navega a la pantalla correspondiente
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true // Guarda el estado de la pestaña anterior
                                }
                                launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
                                restoreState = true // Restaura el estado previo si volvés a esa pantalla
                            }
                        },
                        alwaysShowLabel = false // Oculta el nombre abajo de los iconos
                    )
                }
            }
        }
    ) { innerPadding ->
        // Asocia las rutas con Composable para mostrar cada pantalla
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route, // Primera pantalla que se muestra al abrir la app
            modifier = Modifier.padding(innerPadding) // Respeta el padding del Scaffold
        ) {
            composable(Screen.Search.route) { LoginScreen() } // Mostrar pantalla de search
            composable(Screen.Album.route) { AlbumScreen() } // Muestra pantalla de álbum
            composable(Screen.Exchange.route) { IntercambioScreen() } // Muestra pantalla de intercambio
            composable(Screen.Profile.route) { KioscoScreen() } // Muestra pantalla de perfil
        }
    }
}

// Preview de como se ve
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MisFigusTheme {
        MainScreen()
    }
}
