package com.misfigus.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.AsyncImage
import com.misfigus.dto.UserDto
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun ProfileScreen(
    onChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var user by remember { mutableStateOf<UserDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch {
                val token = UserSessionManager.getToken(context) ?: return@launch
                val stream = context.contentResolver.openInputStream(uri) ?: return@launch
                val requestBody = stream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("image", "profile.jpg", requestBody)

                try {
                    val response = AuthApi.getService(context).uploadProfileImage("Bearer $token", multipart)
                    user = user?.copy(profileImageUrl = response.imageUrl)
                    showImageDialog = false
                } catch (e: Exception) {
                    error = "Error al subir imagen: ${e.message}"
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val token = UserSessionManager.getToken(context)
        if (!token.isNullOrBlank()) {
            try {
                user = AuthApi.getService(context).getCurrentUser()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { showImageDialog = true },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = currentUser.profileImageUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
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

    if (showImageDialog) {
        AlertDialog(
            onDismissRequest = { showImageDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    launcher.launch("image/*")
                }) {
                    Text("Cambiar imagen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImageDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Foto de perfil") },
            text = {
                AsyncImage(
                    model = currentUser.profileImageUrl,
                    contentDescription = "Imagen actual",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    user = user?.copy(fullName = fullName, username = username)
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
