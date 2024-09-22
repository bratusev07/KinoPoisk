package ru.bratusev.data.storage.remote.common

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Common {
    private val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/"

    private val key = "de1db718-950e-449d-88a1-39a41062cee6"

    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)

    object RetrofitClient {
        private var retrofit: Retrofit? = null

        fun getClient(baseUrl: String): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

        private fun getOkHttpClient() = OkHttpClient.Builder()
            .addInterceptor(getInterceptor())
            .build()

        private fun getInterceptor() = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-API-KEY", key)
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }
    }
}