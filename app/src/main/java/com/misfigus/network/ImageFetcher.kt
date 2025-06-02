package com.misfigus.network

import android.content.Context
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun fetchProtectedImageBytes(
    context: Context,
    imageUrl: String
): ByteArray? {
    val token = UserSessionManager.getToken(context) ?: return null

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(imageUrl)
        .addHeader("Authorization", "Bearer $token")
        .build()

    return withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.bytes()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}