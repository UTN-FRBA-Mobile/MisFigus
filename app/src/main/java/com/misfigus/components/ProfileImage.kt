package com.misfigus.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.misfigus.R
import com.misfigus.network.ApiConfig
import com.misfigus.network.TokenProvider
import createImageLoaderWithToken

@Composable
fun ProfileImage(
    imageUrl: String?,
    size: Dp,
    imageLoader: ImageLoader? = null // Opcional
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
            contentDescription = "Profile image",
            imageLoader = loader,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
        )
    } else {
        // Fallback: imagen local o placeholder
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
