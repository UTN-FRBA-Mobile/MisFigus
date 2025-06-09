package com.misfigus.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.misfigus.R
import com.misfigus.models.Album
import com.misfigus.models.AlbumCategoryEnum
import com.misfigus.models.trades.TradingCard
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.screens.album.AlbumDetailScreen
import com.misfigus.screens.album.AlbumsFromCategory
import com.misfigus.screens.album.AlbumsViewModel
import com.misfigus.screens.albums.MyAlbums
import com.misfigus.screens.login.LoginScreen
import com.misfigus.screens.login.PresentationScreen
import com.misfigus.screens.login.RegisterScreen
import com.misfigus.screens.map.MapScreen
import com.misfigus.screens.profile.ProfileScreen
import com.misfigus.screens.trades.IntercambioScreen
import com.misfigus.screens.trades.TraderOptionsScreen
import com.misfigus.screens.trades.requests.TradeRequestDetailScreen
import com.misfigus.screens.trades.requests.TradeRequestsScreen
import com.misfigus.session.SessionViewModel
import com.misfigus.session.UserSessionManager
import com.misfigus.ui.theme.Purple


// cada pestaña de la barra de navegacion
sealed class Screen(val route: String, val iconType: IconType? = null) {
    data object Presentation : Screen("presentation")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Search : Screen("search", IconType.Drawable(R.drawable.search_icon))
    data object AlbumCategory : Screen("category/{category}", IconType.Drawable(R.drawable.album_icon))
    data object Trading : Screen("trading", IconType.Drawable(R.drawable.trading_icon))
    data object Profile : Screen("profile", IconType.Drawable(R.drawable.profile_icon))
    data object AlbumDetails : Screen("details/{albumId}", IconType.Drawable(R.drawable.album_icon))
    data object Albums: Screen("album", IconType.Drawable(R.drawable.album_icon))
    data object TradeRequests : Screen("trade_requests")
    data object TradeRequestDetail : Screen("trade_request_detail/{requestId}")

}

sealed class IconType {
    data class Vector(val icon: androidx.compose.ui.graphics.vector.ImageVector) : IconType()
    data class Drawable(val resId: Int) : IconType()
}

val screens = listOf(
    Screen.Search,
    Screen.Albums,
    Screen.Trading,
    Screen.Profile
)

fun matchesRoute(currentRoute: String?, baseRoute: String): Boolean {
    if (currentRoute == null) return false
    return when (baseRoute) {
        "album" -> currentRoute.indexOf("album") == 0 || currentRoute.indexOf("category") == 0
                || currentRoute.indexOf("details") == 0
        else -> currentRoute.indexOf(baseRoute) == 0
    }
}

@Composable
fun AppNavigation(navController: NavHostController, sessionViewModel: SessionViewModel) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val albumsViewModel: AlbumsViewModel = viewModel()

    val context = LocalContext.current
    val authService = AuthApi.getService(context)
    val sessionManager = UserSessionManager

    val bottomBarBaseRoutes = listOf(
        "search", "album", "trading", "profile", "details", "category", "trade_requests", "trade_request_detail"
    )

    Scaffold(
        bottomBar = {
            if (bottomBarBaseRoutes.any { matchesRoute(currentRoute, it) }) {
                NavigationBar(containerColor = Color.White) {
                    screens.forEach { screen ->
                        val isSelected = matchesRoute(currentRoute, screen.route)
                        NavigationBarItem(
                            icon = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    when (val icon = screen.iconType) {
                                        is IconType.Vector -> Icon(icon.icon, contentDescription = null)
                                        is IconType.Drawable -> Icon(
                                            painter = painterResource(id = icon.resId),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = if (isSelected) Purple else Color.Gray
                                        )
                                        null -> {}
                                    }

                                    if (isSelected) {
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .padding(top = 8.dp),
                                            thickness = 2.dp,
                                            color = Purple
                                        )
                                    }
                                }
                            },
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (TokenProvider.token != null) Screen.Albums.route else "presentation",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Presentation.route) { PresentationScreen(navController) }
            composable(Screen.Login.route) { LoginScreen(navController, sessionViewModel) }
            composable(Screen.Register.route) { RegisterScreen(navController, sessionViewModel) }
            composable(Screen.Search.route) { MapScreen() }
            composable(Screen.Albums.route) { MyAlbums(navController, albumsViewModel.categoriesUiState, albumsViewModel.albumsUiState) }
            composable(Screen.Trading.route) { IntercambioScreen(navController) }
            composable("trader/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                id?.let {
                    TraderOptionsScreen(navHostController = navController, id = it)
                }
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    sessionViewModel = sessionViewModel,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.AlbumDetails.route) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("albumId")
                albumId?.let {
                    val albumDetailed = getAlbumByName(it)
                    if(albumDetailed != null) AlbumDetailScreen(navController, album = albumDetailed, albumsViewModel)
                }
            }
            composable(Screen.AlbumCategory.route) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("category") // @Orne
                val categoryEnum = categoryName?.let { AlbumCategoryEnum.valueOf(it) }
                categoryEnum?.let {
                    AlbumsFromCategory(navController, it, albumsViewModel)
                }
            }
            composable(Screen.TradeRequests.route) {
                TradeRequestsScreen(navController, sessionViewModel)
            }
            composable(Screen.TradeRequestDetail.route) { backStackEntry ->
                val requestId = backStackEntry.arguments?.getString("requestId")
                requestId?.let {
                    TradeRequestDetailScreen(requestId = it, navController = navController)
                }
            }
        }
    }
}

@Composable
fun getAlbumByName(name: String): Album? {
    return mockedAlbums().find { it.albumId == name }
}

@Composable
fun mockedAlbums(): List<Album> {
    val tradingCardsQatar = listOf(
        TradingCard(1, "Qatar 2022", obtained = true, 3),
        TradingCard(2, "Qatar 2022", obtained = false, repeatedQuantity = 0),
        TradingCard(3, "Qatar 2022", obtained = true, repeatedQuantity = 0)
    )
    val tradingCardsSouthAfrica = listOf(
        TradingCard(1, "South Africa 2010", obtained = true, 2),
        TradingCard(2, "South Africa 2010", obtained = true, repeatedQuantity = 0),
    )

    return listOf(
        Album(1, "Qatar 2022", "Fifa World Cup Qatar 2022", tradingCardsQatar, false, AlbumCategoryEnum.FOOTBALL, "qatar", 2022, 670),
        Album(2, "South Africa 2010", "Fifa World Cup South Africa 2010", tradingCardsSouthAfrica, true, AlbumCategoryEnum.FOOTBALL, "south_africa", 2010, 638),
        Album(3, "Ice Age", "La Era de Hielo: Choque de Mundos", emptyList(), false, AlbumCategoryEnum.MOVIES, "ice_age", 2022, 332)
    )
}
