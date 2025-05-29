package com.misfigus.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponseDto(
    @SerialName("imageUrl") val imageUrl: String
)