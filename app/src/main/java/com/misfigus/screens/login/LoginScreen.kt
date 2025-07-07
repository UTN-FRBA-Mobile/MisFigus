package com.misfigus.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.misfigus.R
import com.misfigus.dto.UserLoginDto
import com.misfigus.navigation.Screen
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.session.SessionViewModel
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


@Composable
fun LoginScreen(navController: NavController, sessionViewModel: SessionViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Bienvenido,",
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.regular),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Iniciá sesión para continuar",
            fontSize = 26.sp,
            color = colorResource(id = R.color.regular),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("mail@ejemplo.com") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            trailingIcon = {
                if (email.contains("@")) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingresá tu contraseña") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = colorResource(id = R.color.purple),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    // Change password
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current
        // Login button
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        Log.d("LoginScreen", "Intentando iniciar sesión con email: $email")
                        val loginDto = UserLoginDto(email = email, password = password)
                        Log.d("LoginScreen", "creelogindto")
                        Log.d("LoginScreen", "loginDto: $loginDto")
                        
                        val response = AuthApi.getService(context).login(loginDto)
                        Log.d("LoginScreen", "Login exitoso")
                        Log.d("LoginScreen", "Reponse: ${response}")

                        // Guardar el token globalmente
                        TokenProvider.token = response.token
                        UserSessionManager.saveToken(context, response.token) // contexto  guardado

                        // Obtener usuario actual
                        val currentUser = AuthApi.getService(context).getCurrentUser()

                        // Volver al hilo principal para navegar
                        withContext(Dispatchers.Main) {
                            sessionViewModel.updateUser(currentUser)
                            loginError = null
                            navController.navigate(Screen.Albums.route) {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    catch (e: retrofit2.HttpException) {
                        withContext(Dispatchers.Main) {
                            Log.d("LoginScreen", "Error de autenticación: ${e.response()?.errorBody()?.string()}")
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorMessage = try {
                                JSONObject(errorBody).getString("message")
                            } catch (e: Exception) {
                                "Error desconocido"
                            }
                            loginError = errorMessage
                        }
                    }
                    catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            loginError = "Error de conexión: ${e.message}"
                        }
                    }
                }
            },
            modifier = Modifier
                .width(217.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar sesión", color = Color.White)
        }
        loginError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("¿No tenés una cuenta?", color = colorResource(id = R.color.regular), fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Registrate",
                    color = colorResource(id = R.color.purple),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}
