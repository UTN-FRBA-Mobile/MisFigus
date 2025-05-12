package com.misfigus.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Excluir endpoints públicos
        val isPublicEndpoint = url.contains("/users/login") || url.contains("/users/register")

        return if (!isPublicEndpoint && TokenProvider.token != null) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenProvider.token}")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}

