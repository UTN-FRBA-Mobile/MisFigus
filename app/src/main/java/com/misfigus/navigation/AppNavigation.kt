package com.misfigus.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.misfigus.screens.album.AlbumUserUiState
import com.misfigus.screens.album.AlbumsFromCategory
import com.misfigus.screens.album.AlbumsViewModel
import com.misfigus.screens.albums.MyAlbums
import com.misfigus.screens.login.LoginScreen
import com.misfigus.screens.login.PresentationScreen
import com.misfigus.screens.login.RegisterScreen
import com.misfigus.screens.map.MapScreen
import com.misfigus.screens.profile.ProfileScreen
import com.misfigus.screens.trades.IntercambioScreen
import com.misfigus.screens.trades.TradeViewModel
import com.misfigus.screens.trades.TraderOptionsScreen
import com.misfigus.screens.trades.requests.AcceptOrReject
import com.misfigus.screens.trades.requests.TradeRequestsScreen
import com.misfigus.session.SessionViewModel
import com.misfigus.session.UserSessionManager
import com.misfigus.ui.theme.Purple

sealed class Screen(val route: String, val iconType: IconType? = null) {
    data object Presentation : Screen("presentation")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Search : Screen("search", IconType.Drawable(R.drawable.search_icon))
    data object AlbumCategory : Screen("category/{category}", IconType.Drawable(R.drawable.album_icon))
    data object Trading : Screen("trading", IconType.Drawable(R.drawable.trading_icon))
    data object Profile : Screen("profile", IconType.Drawable(R.drawable.profile_icon))
    data object AlbumDetails : Screen("details/{id}", IconType.Drawable(R.drawable.album_icon))
    data object Albums: Screen("album", IconType.Drawable(R.drawable.album_icon))
    data object TradeRequests : Screen("trade_requests")
    data object TradeRequestDetail : Screen("trade_request_detail")

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
        "trading" -> currentRoute.indexOf("trading") == 0 || currentRoute.indexOf("trade_requests") == 0
                || currentRoute.indexOf("trade_request_detail") == 0 || currentRoute.indexOf("trader") == 0
        else -> currentRoute.indexOf(baseRoute) == 0
    }
}

@Composable
fun AppNavigation(navController: NavHostController, sessionViewModel: SessionViewModel) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val albumsViewModel: AlbumsViewModel = viewModel()
    val tradeViewModel: TradeViewModel = viewModel()

    val context = LocalContext.current
    val authService = AuthApi.getService(context)
    val sessionManager = UserSessionManager

    val bottomBarBaseRoutes = listOf(
        "search", "album", "trading", "profile", "details", "category", "trade_requests", "trade_request_detail"
    )

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            )
        },
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
            composable(Screen.Albums.route) { MyAlbums(navController, albumsViewModel, sessionViewModel) }
            composable(Screen.Trading.route) { IntercambioScreen(navController, sessionViewModel, tradeViewModel) }
            composable("trader") {
                TraderOptionsScreen(navHostController = navController, tradeViewModel = tradeViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    sessionViewModel = sessionViewModel,
                    albumsViewModel = albumsViewModel,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.AlbumDetails.route) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("id")
                if(albumId != null){
                    val albumUiState = albumsViewModel.albumUserUiState

                    LaunchedEffect(albumId) {
                        albumsViewModel.clearAlbumState()
                        albumsViewModel.getUserAlbum(albumId)
                    }

                    when (albumUiState) {
                        is AlbumUserUiState.Loading -> {
                            Text(text = "Api call loading... [GET ALBUM BY ID]")
                        }

                        is AlbumUserUiState.Success -> {
                            albumId?.let {
                                AlbumDetailScreen(navController, initialAlbum = albumUiState.album, albumsViewModel)
                            }
                        }
                        is AlbumUserUiState.Error -> {
                            Text(text = "Api call error [GET ALBUM BY ID]")
                        }

                    }
                }

            }
            composable(Screen.AlbumCategory.route) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("category")
                val categoryEnum = categoryName?.let { AlbumCategoryEnum.valueOf(it) }
                categoryEnum?.let {
                    AlbumsFromCategory(navController, it, albumsViewModel)
                }
            }
            composable(Screen.TradeRequests.route) {
                TradeRequestsScreen(navController, sessionViewModel, tradeViewModel)
            }
            composable(Screen.TradeRequestDetail.route) { backStackEntry ->
                AcceptOrReject(navHostController = navController, tradeViewModel = tradeViewModel, sessionViewModel = sessionViewModel)
            }
        }
    }
}
