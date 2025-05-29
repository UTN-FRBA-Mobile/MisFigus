import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.misfigus.network.AuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getInstance(context: Context): Retrofit {
        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // ✅ contexto disponible acá
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun <T> create(serviceClass: Class<T>, context: Context): T {
        return getInstance(context).create(serviceClass)
    }
}
