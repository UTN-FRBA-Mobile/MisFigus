package com.misfigus.session

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.misfigus.dto.UserDto
import com.misfigus.network.AuthApiService

// This class is used during the session to store the user's data, and in that way the backend doesn't have to be called every time.
class SessionViewModel(
    private val authService: AuthApiService,
    private val sessionManager: UserSessionManager
) : ViewModel() {

    var user: UserDto? = null
        private set

    suspend fun loadUserIfNotLoaded(context: Context) {
        if (user == null) {
            val token = sessionManager.getToken(context)
            if (!token.isNullOrEmpty()) {
                user = authService.getCurrentUser()
            }
        }
    }

    suspend fun refreshUser(context: Context) {
        val token = sessionManager.getToken(context)
        if (!token.isNullOrEmpty()) {
            user = authService.getCurrentUser()
        }
    }

    suspend fun logout(context: Context) {
        sessionManager.clearToken(context)
        user = null
    }

    fun updateUser(userDto: UserDto) {
        user = userDto
    }
}
