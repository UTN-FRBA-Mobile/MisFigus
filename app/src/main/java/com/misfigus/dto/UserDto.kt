package com.misfigus.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    val fullName: String,
    val username: String,
    val profileImageUrl: String? = null
)
