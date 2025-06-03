package com.misfigus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.misfigus.navigation.AppNavigation
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.session.SessionViewModel
import com.misfigus.session.UserSessionManager
import com.misfigus.ui.theme.MisFigusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionViewModel = SessionViewModel(
            authService = AuthApi.getService(this),
            sessionManager = UserSessionManager
        )

        CoroutineScope(Dispatchers.IO).launch {
            val token = UserSessionManager.getToken(this@MainActivity)

            if (!token.isNullOrBlank()) {
                try {
                    val user = AuthApi.getService(this@MainActivity).getCurrentUser()
                    TokenProvider.token = token
                    sessionViewModel.updateUser(user)
                } catch (e: Exception) {
                    UserSessionManager.clearToken(this@MainActivity)
                    TokenProvider.token = null
                }
            }

            withContext(Dispatchers.Main) {
                setContent {
                    MisFigusTheme {
                        val navController = rememberNavController()
                        AppNavigation(navController, sessionViewModel)
                    }
                }
            }
        }
    }
}
