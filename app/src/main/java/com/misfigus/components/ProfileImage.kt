package com.misfigus.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.misfigus.R
import com.misfigus.network.TokenProvider
import createImageLoaderWithToken

@Composable
fun ProfileImage(
    imageUrl: String?,
    size: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String = "Foto de perfil",
    imageLoader: ImageLoader? = null
)
 {

    val token = TokenProvider.token
    val context = LocalContext.current
    if (imageUrl != null && token != null) {
        val loader = imageLoader ?: createImageLoaderWithToken(context, token)


        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            imageLoader = loader,
            contentDescription = contentDescription,
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            fallback = painterResource(id = R.drawable.default_profile),
            error = painterResource(id = R.drawable.default_profile),
            placeholder = painterResource(id = R.drawable.default_profile)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.default_profile),
            contentDescription = "Imagen por defecto",
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    }
}

