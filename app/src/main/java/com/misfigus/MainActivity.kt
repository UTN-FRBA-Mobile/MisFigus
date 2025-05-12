package com.misfigus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.misfigus.models.User
import com.misfigus.models.UserRepository
import com.misfigus.navigation.AppNavigation
import com.misfigus.network.TokenProvider
import com.misfigus.session.UserSessionManager
import com.misfigus.ui.theme.MisFigusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val token = UserSessionManager.getToken(this@MainActivity)
            if (!token.isNullOrBlank()) {
                TokenProvider.token = token
            }

            withContext(Dispatchers.Main) {
                setContent {
                    MisFigusTheme {
                        val navController = rememberNavController()
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}




// Preview de como se ve
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MainScreenPreview() {
//    MisFigusTheme {
//        val navController = rememberNavController()
//                AppNavigation(navController)
//    }
//}
