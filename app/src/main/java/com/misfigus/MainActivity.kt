package com.misfigus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.misfigus.models.Album
import com.misfigus.retrofit.RetrofitClient
import com.misfigus.models.User
import com.misfigus.models.UserRepository
import com.misfigus.navigation.AppNavigation
import com.misfigus.ui.theme.MisFigusTheme

// Clase principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchData()

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

    private fun fetchData() {
        val call = RetrofitClient.instance.getData()
        call.enqueue(object : retrofit2.Callback<List<Album>> {
            override fun onResponse(
                call: retrofit2.Call<List<Album>>,
                response: retrofit2.Response<List<Album>>
            ) {
                if (response.isSuccessful) {
                    val albums = response.body()
                    // Do something with the list of albums
                    Log.d("API_SUCCESS", "API call successful: ${albums?.size} albums received")
                    albums?.forEach { album ->
                        Log.d("API_SUCCESS", "Album:")
                    }
                } else {
                    Log.e("API_ERROR", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(
                call: retrofit2.Call<List<Album>>,
                t: Throwable
            ) {
                Log.e("API_FAILURE", "API call failed: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
