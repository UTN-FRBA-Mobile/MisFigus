import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import okhttp3.OkHttpClient
import java.io.File

fun createImageLoaderWithToken(context: Context, token: String): ImageLoader {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    return ImageLoader.Builder(context)
        .okHttpClient(okHttpClient)
        .diskCache {
            DiskCache.Builder()
                .directory(File(context.cacheDir, "image_cache"))
                .maxSizeBytes(50L * 1024 * 1024) // 50 MB
                .build()
        }
        .build()
}
