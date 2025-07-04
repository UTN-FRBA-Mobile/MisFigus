package com.misfigus.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class LoginResponseDto(
    val token: String,
    val email: String,
    val fullName: String,
    val username: String,
    //val albums: JsonElement,
    val friends: List<String>,
)
