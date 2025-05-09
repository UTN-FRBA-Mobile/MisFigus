package com.misfigus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.misfigus.R
import com.misfigus.models.Album
import com.misfigus.models.TradingCard
import com.misfigus.screens.AlbumDetailScreen
import com.misfigus.screens.AlbumScreen
import com.misfigus.screens.IntercambioScreen
import com.misfigus.screens.KioscoScreen
import com.misfigus.screens.LoginScreen


// Cada data class representa cada pestaña de la barra de navegacion
sealed class Screen(val route: String, val iconType: IconType) {
    data object Search : Screen("search", IconType.Vector(Icons.Default.Search))
    data object Album : Screen("album", IconType.Drawable(R.drawable.ic_menu_book))
    data object Trading : Screen("trading", IconType.Drawable(R.drawable.ic_swap))
    data object Profile : Screen("profile", IconType.Vector(Icons.Default.Person))
    data object AlbumDetails : Screen("details/{albumId}", IconType.Drawable(R.drawable.ic_launcher_foreground))
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
    Screen.Trading,
    Screen.Profile
)

// Función Composable con la lógica de navegación y la barra inferior
@Composable
fun AppNavigation(navController: NavHostController) {
    val currentBackStack by navController.currentBackStackEntryAsState() // Historial de navegación para conseguir la pantalla actual
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
                            // Verifica si ya estás en la pantalla actual
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) { // Navega a la pantalla correspondiente
                                    launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
                                    restoreState = true // Restaura el estado previo si volvés a esa pantalla
                                }
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
            startDestination = Screen.Album.route, // Primera pantalla que se muestra al abrir la app
            modifier = Modifier.padding(innerPadding) // Respeta el padding del Scaffold
        ) {
            composable(Screen.Search.route) { LoginScreen() } // Mostrar pantalla de search
            composable(Screen.Album.route) { AlbumScreen(navController) } // Muestra pantalla de álbum
            composable(Screen.Trading.route) { IntercambioScreen() } // Muestra pantalla de intercambio
            composable(Screen.Profile.route) { KioscoScreen() } // Muestra pantalla de perfil
            composable(Screen.AlbumDetails.route) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("albumId")
                albumId?.let {
                    val albumdetailed = getAlbumByName(it)
                    if(albumdetailed != null) AlbumDetailScreen(album = albumdetailed)
                }
            }

        }
    }
}

@Composable
fun getAlbumByName(name: String): Album? {
    return mockedAlbums().find{ it.albumId == name }
}

@Composable
fun mockedAlbums(): List<Album>{
    val tradingCards = listOf(
            TradingCard(1, "albumA", obtained = true, 3),
            TradingCard(2, "albumA", obtained = false, repeatedQuantity = 0),
            TradingCard(3, "albumA", obtained = true, repeatedQuantity = 0)
        )


    val albums = listOf(
            Album("Qatar 2022", tradingCards, false),
            Album("Ice Age", emptyList(), false)
        )


    return albums
}
