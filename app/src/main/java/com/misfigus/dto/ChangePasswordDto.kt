package com.misfigus.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDto(
    val currentPassword: String,
    val newPassword: String
)
