package com.misfigus.dto

import com.misfigus.models.AlbumCategoryEnum
import kotlinx.serialization.Serializable

@Serializable
data class AlbumCategoryCountDto(
    val category: AlbumCategoryEnum,
    val count: Int
)