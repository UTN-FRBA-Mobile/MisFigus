package com.misfigus.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.indicatorColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.misfigus.R
import com.misfigus.models.Album
import com.misfigus.models.AlbumCategoryEnum
import com.misfigus.models.TradingCard
import com.misfigus.screens.AlbumDetailScreen
import com.misfigus.screens.AlbumsScreen
import com.misfigus.screens.CategoryScreen
import com.misfigus.screens.IntercambioScreen
import com.misfigus.screens.KioscoScreen
import com.misfigus.screens.LoginScreen
import com.misfigus.screens.PresentationScreen
import com.misfigus.screens.RegisterScreen
import com.misfigus.ui.theme.EditColor


// Cada data class representa cada pestaña de la barra de navegacion
sealed class Screen(val route: String, val iconType: IconType) {
    data object Search : Screen("search", IconType.Drawable(R.drawable.search_icon))
    data object AlbumCategory : Screen("{category}", IconType.Drawable(R.drawable.ic_menu_book))
    data object Trading : Screen("trading", IconType.Drawable(R.drawable.trading_icon))
    data object Profile : Screen("profile", IconType.Drawable(R.drawable.profile_icon))
    data object AlbumDetails : Screen("details/{albumId}", IconType.Drawable(R.drawable.ic_launcher_foreground))
    data object Albums: Screen("album", IconType.Drawable(R.drawable.album_icon))
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
    Screen.Albums,
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
            NavigationBar(containerColor = Color.White) {
                screens.forEach { screen -> // Recorre todas las pestañas
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                when (val icon = screen.iconType) {
                                    is IconType.Vector -> Icon(icon.icon, contentDescription = null)
                                    is IconType.Drawable -> Icon(
                                        painter = painterResource(id = icon.resId),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = if(isSelected) EditColor else Color.Gray
                                    )
                                }

                                if(isSelected){
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(top = 8.dp),
                                        thickness = 2.dp,
                                        color = EditColor
                                    )
                                }
                            }

                        },
                        selected = isSelected, // Marca como seleccionada la pestaña actual

                        onClick = {
                            // Verifica si ya estás en la pantalla actual
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) { // Navega a la pantalla correspondiente
                                    launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
                                    restoreState = true // Restaura el estado previo si volvés a esa pantalla
                                }
                            }
                        },
                        alwaysShowLabel = false, // Oculta el nombre abajo de los iconos
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent // Evita el fondo redondo
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        // Asocia las rutas con Composable para mostrar cada pantalla
        NavHost(
            navController = navController,
            startDestination = "presentation", // Primera pantalla que se muestra al abrir la app
            modifier = Modifier.padding(innerPadding) // Respeta el padding del Scaffold
        ) {
            composable("presentation") {PresentationScreen(navController)}
            composable("login") {LoginScreen(navController)}
            composable("register") {RegisterScreen(navController)}
            composable(Screen.Albums.route) { CategoryScreen(navController) } // Muestra pantalla de álbum
            composable(Screen.Trading.route) { IntercambioScreen() } // Muestra pantalla de intercambio
            composable(Screen.Profile.route) { KioscoScreen() } // Muestra pantalla de perfil
            composable(Screen.AlbumDetails.route) { backStackEntry ->  //Muestra detalles de un album
                val albumId = backStackEntry.arguments?.getString("albumId")
                albumId?.let {
                    val albumDetailed = getAlbumByName(it)
                    if(albumDetailed != null) AlbumDetailScreen(navController, album = albumDetailed)
                }
            }

            composable(Screen.AlbumCategory.route) { backStackEntry ->  //Muestra albumes de una categoria
                val category = backStackEntry.arguments?.getString("category")
                category?.let {
                    AlbumsScreen(navController, it)
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
            TradingCard(1, "Qatar 2022", obtained = true, 3),
            TradingCard(2, "Qatar 2022", obtained = false, repeatedQuantity = 0),
            TradingCard(3, "Qatar 2022", obtained = true, repeatedQuantity = 0)
        )

    val albums = listOf(
            Album("Qatar 2022", tradingCards, false, AlbumCategoryEnum.FUTBOL),
            Album("Ice Age", emptyList(), false, AlbumCategoryEnum.PELICULAS)
        )

    return albums
}

