package com.example.sistemastreaming.Networkings
import AuthInterceptor
import com.example.sistemastreaming.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    var jwtToken: String? = null
        set(value) {
            field = value
            retrofit = createRetrofit()
        }

    private fun getClient(): OkHttpClient {
        val builder = getUnsafeOkHttpClient().newBuilder()
        jwtToken?.let { token ->
            builder.addInterceptor(AuthInterceptor(token))
            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    private var retrofit: Retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
