package com.misfigus.dto

import kotlinx.serialization.Serializable

@Serializable
data class StickerDto(
    val number: Int,
    val repeatCount: Int
)

@Serializable
data class UserAlbumDto(
    val id: String,
    val stickers: List<StickerDto>,
)

@Serializable
data class UserDto(
    val email: String,
    val fullName: String,
    val username: String,
    val profileImageUrl: String? = null,
    val location: String? = null,
    val shipping: Boolean? = null,
    val reputation: String? = null,
    val albums: List<UserAlbumDto> = emptyList(),
    val friends: List<String> = emptyList()
)

