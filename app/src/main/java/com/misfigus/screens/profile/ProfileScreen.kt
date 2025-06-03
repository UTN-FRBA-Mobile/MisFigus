package com.misfigus.screens.profile

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.misfigus.dto.ChangePasswordDto
import com.misfigus.dto.UserDto
import com.misfigus.network.AuthApi
import com.misfigus.network.TokenProvider
import com.misfigus.network.fetchProtectedImageBytes
import com.misfigus.session.SessionViewModel
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun ProfileScreen(sessionViewModel: SessionViewModel, onLogout: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var error by remember { mutableStateOf<String?>(null) }
    var profileImageBytes by remember { mutableStateOf<ByteArray?>(null) }

    var showImageDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showIncorrectPasswordDialog by remember { mutableStateOf(false) }

    val user = sessionViewModel.user

    LaunchedEffect(Unit) {
        try {
            if (user == null) {
                sessionViewModel.loadUserIfNotLoaded(context)
            }
        } catch (e: Exception) {
            error = "Error al cargar usuario: ${e.message}"
        }
    }

    if (sessionViewModel.user == null) {
        if (error != null) {
            Text(error!!, color = Color.Red)
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
        }
        return
    }

    if (error != null) {
        Text(error!!, color = Color.Red)
        return
    }

    val currentUser = sessionViewModel.user!!
    var fullName by remember { mutableStateOf(currentUser.fullName) }
    var username by remember { mutableStateOf(currentUser.username) }

    LaunchedEffect(currentUser.profileImageUrl) {
        profileImageBytes = fetchProtectedImageBytes(context, currentUser.profileImageUrl!!)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch {
                val stream = context.contentResolver.openInputStream(uri) ?: return@launch
                val requestBody = stream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                val fileName = (currentUser.fullName ?: "imagen") + ".jpg"
                val multipart = MultipartBody.Part.createFormData("image", fileName, requestBody)

                try {
                    val response = AuthApi.getService(context).uploadProfileImage(multipart)
                    sessionViewModel.updateUser(currentUser.copy(profileImageUrl = response.imageUrl))
                    showImageDialog = false
                } catch (e: Exception) {
                    error = "Error al subir imagen: ${e.message}"
                }
            }
        }
    }

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
            if (profileImageBytes != null) {
                Image(
                    bitmap = BitmapFactory.decodeByteArray(profileImageBytes, 0, profileImageBytes!!.size).asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } else {
                CircularProgressIndicator()
            }
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

        OutlinedButton(onClick = { showChangePasswordDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    TokenProvider.token = null
                    UserSessionManager.clearToken(context)
                    sessionViewModel.logout(context)
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
                if (profileImageBytes != null) {
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(profileImageBytes, 0, profileImageBytes!!.size).asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        val updated = UserDto(currentUser.email, fullName, username, currentUser.profileImageUrl)
                        val hasChanges = updated.fullName != currentUser.fullName || updated.username != currentUser.username

                        if (!hasChanges) {
                            showEditDialog = false // Cierra el diálogo sin hacer nada
                            return@launch
                        }
                        try {
                            val response = AuthApi.getService(context).updateCurrentUser(updated)
                            sessionViewModel.updateUser(response)
                            showEditDialog = false
                        } catch (e: Exception) {
                            error = "Error al actualizar perfil: ${e.message}"
                        }
                    }
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

    if (showChangePasswordDialog) {
        var currentPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var passwordValidationError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    // Validación frontend
                    if (!newPassword.any { it.isDigit() } || newPassword.count { it.isLetter() } <= 5) {
                        passwordValidationError = "La contraseña debe tener al menos un número y más de 5 letras."
                        return@TextButton
                    }

                    coroutineScope.launch {
                        try {
                            val dto = ChangePasswordDto(currentPassword, newPassword)
                            AuthApi.getService(context).changePassword(dto)
                            showChangePasswordDialog = false
                            passwordValidationError = null
                            currentPassword = ""
                            newPassword = ""
                        }
                        catch (e: Exception) {
                            if (e.message?.contains("400") == true || e.message?.contains("contraseña actual") == true) {
                                showIncorrectPasswordDialog = true
                            } else {
                                error = "Error al cambiar contraseña: ${e.message}"
                            }
                        }
                    }
                }) {
                    Text("Cambiar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Cambiar contraseña") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Contraseña actual") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nueva contraseña") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    passwordValidationError?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            }
        )
    }

    if (showIncorrectPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showIncorrectPasswordDialog = false },
            title = { Text("Contraseña incorrecta") },
            text = { Text("La contraseña actual ingresada no es correcta. Inténtalo de nuevo.") },
            confirmButton = {
                TextButton(onClick = { showIncorrectPasswordDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }

}