package com.misfigus.models

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val fullName: String,
    val username: String,
    val password: String,
    val profilePictureUrl: String? = null,
    val registrationTimestamp: Long = System.currentTimeMillis()
)