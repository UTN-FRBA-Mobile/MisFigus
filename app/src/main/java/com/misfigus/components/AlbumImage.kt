package com.misfigus.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.misfigus.network.ApiConfig
import com.misfigus.network.TokenProvider
import createImageLoaderWithToken

@Composable
fun AlbumImage(
    imageUrl: String?,
    size: Dp,
    imageLoader: ImageLoader? = null
) {
    val context = LocalContext.current

    val loader = remember(imageLoader, TokenProvider.token) {
        imageLoader ?: TokenProvider.token?.let { createImageLoaderWithToken(context, it) }
    }

    val fullUrl = remember(imageUrl) {
        ApiConfig.buildImageUrl(imageUrl)
    }

    if (loader != null && !fullUrl.isNullOrBlank()) {
        val request = ImageRequest.Builder(context)
            .data(fullUrl)
            .crossfade(true)
            .build()

        AsyncImage(
            model = request,
            contentDescription = "Imagen de álbum",
            imageLoader = loader,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp))
        )
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.PhotoAlbum,
                contentDescription = "Imagen no disponible",
                tint = Color.White
            )
        }
    }
}
