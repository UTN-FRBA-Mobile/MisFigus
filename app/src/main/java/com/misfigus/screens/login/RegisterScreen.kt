package com.misfigus.screens.login

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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.misfigus.R
import com.misfigus.dto.UserRegisterDto
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
fun RegisterScreen(navController: NavController, sessionViewModel: SessionViewModel) {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var registrationError by remember { mutableStateOf<String?>(null) }
    var registrationSuccess by remember { mutableStateOf(false) }

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
                text = "Creá tu cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.regular),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico*") },
                placeholder = { Text("mail@ejemplo.com") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                trailingIcon = {
                    if (email.contains("@")) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre completo
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre completo*") },
                placeholder = { Text("Ej: Pedro Gómez") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario*") },
                placeholder = { Text("Ej: pedro123") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña*") },
                placeholder = { Text("Ingresá tu contraseña") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Requisitos de contraseña
            Text(
                text = "Debe contener al menos un número y más de 5 letras",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current
            // Botón de registro
            Button(
                onClick = {
                    if (!email.contains("@") || !email.contains(".")) {
                        registrationError = "Correo inválido."
                    } else if (!password.any { it.isDigit() } || password.count { it.isLetter() } <= 5) {
                        registrationError = "La contraseña debe tener al menos un número y más de 5 letras."
                    } else if (fullName.isBlank() || username.isBlank()) {
                        registrationError = "Completá todos los campos obligatorios."
                    } else if (username.contains(" ")) {
                        registrationError = "El nombre de usuario no puede tener espacios."
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val newUser = UserRegisterDto(
                                    email = email,
                                    fullName = fullName,
                                    username = username,
                                    password = password
                                )

                                val response = AuthApi.getService(context).register(newUser)
                                TokenProvider.token = response.token

                                UserSessionManager.saveToken(context, response.token)

                                val currentUser = AuthApi.getService(context).getCurrentUser()

                                sessionViewModel.updateUser(currentUser)

                                withContext(Dispatchers.Main) {
                                    registrationSuccess = true
                                    navController.navigate(Screen.Albums.route) {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            } catch (e: retrofit2.HttpException) {
                                val errorBody = e.response()?.errorBody()?.string()
                                val message = try {
                                    JSONObject(errorBody).getString("message")
                                } catch (_: Exception) {
                                    "Error inesperado del servidor."
                                }

                                withContext(Dispatchers.Main) {
                                    registrationError = message
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    registrationError = "Error de red: ${e.message}"
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(217.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarme", color = Color.White)
            }

            registrationError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Al registrarte aceptás los términos y condiciones",
                fontSize = 16.sp,
                color = colorResource(id = R.color.regular),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("¿Tenés una cuenta?", color = colorResource(id = R.color.regular), fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Iniciá sesión",
                    color = colorResource(id = R.color.purple),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}
