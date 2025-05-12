package com.misfigus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.misfigus.dto.UserDto
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    var user by remember { mutableStateOf<UserDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val token = UserSessionManager.getToken(context)
        if (!token.isNullOrBlank()) {
            try {
                user = AuthApi.retrofitService.getCurrentUser()
            } catch (e: Exception) {
                error = "No se pudo obtener el perfil: ${e.message}"
            }
        } else {
            error = "No hay sesión activa"
        }
    }

    if (error != null) {
        Text(error!!, color = Color.Red)
        return
    }

    if (user == null) {
        CircularProgressIndicator()
        return
    }

    val currentUser = user!!

    var fullName by remember { mutableStateOf(currentUser.fullName) }
    var username by remember { mutableStateOf(currentUser.username) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto de perfil
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { /* cambiar foto más adelante */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "@$username", fontSize = 16.sp, color = Color.Gray)
        Text(text = currentUser.email, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(onClick = { showEditDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Editar nombre y usuario")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onChangePassword, modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(24.dp))

        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                coroutineScope.launch {
                    TokenProvider.token = null
                    UserSessionManager.clearToken(context)
                    onLogout()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión", color = Color.White)
        }
    }

    // Diálogo para editar nombre y username
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    // editar fullname y username
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Editar perfil") },
            text = {
                Column {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
