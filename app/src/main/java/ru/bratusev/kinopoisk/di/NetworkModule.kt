package ru.bratusev.kinopoisk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.bratusev.data.storage.remote.common.RetrofitServices
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String = "https://kinopoiskapiunofficial.tech/api/v2.2/"

    @Provides
    @Named("key")
    fun provideKey(): String = "de1db718-950e-449d-88a1-39a41062cee6"

    @Provides
    @Singleton
    fun provideInterceptor(
        @Named("key") key: String,
    ): Interceptor =
        Interceptor { chain ->
            val original = chain.request()
            val request =
                original
                    .newBuilder()
                    .header("X-API-KEY", key)
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): RetrofitServices = retrofit.create(RetrofitServices::class.java)
}
