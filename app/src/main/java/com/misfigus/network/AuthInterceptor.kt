package com.misfigus.network

import android.content.Context
import com.misfigus.session.UserSessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        val token = runBlocking { UserSessionManager.getToken(context) }

        val isPublicEndpoint = url.contains("/users/login") || url.contains("/users/register")

        return if (!isPublicEndpoint && !token.isNullOrBlank()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
