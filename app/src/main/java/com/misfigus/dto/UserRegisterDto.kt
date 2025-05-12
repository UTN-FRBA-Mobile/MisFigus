package com.misfigus.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDto(
    val email: String,
    val fullName: String,
    val username: String,
    val password: String
)
