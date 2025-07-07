package com.misfigus.network

object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8080"

    fun buildImageUrl(path: String?): String? {
        return when {
            path.isNullOrBlank() -> null
            path.startsWith("http") -> path
            else -> BASE_URL + path
        }
    }
}