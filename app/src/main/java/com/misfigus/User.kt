package com.misfigus

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.UUID

data class User @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String = UUID.randomUUID().toString(), // Unique user ID (UUID by default)
    val email: String,
    val username: String,
    val password: String, // Store a hashed password, not plain text!
    val profilePictureUrl: String? = null, // Optional profile picture URL
    val registrationDate: LocalDateTime = LocalDateTime.now() // Date of registration
)