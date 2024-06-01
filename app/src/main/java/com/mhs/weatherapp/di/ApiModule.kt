package com.mhs.weatherapp.di

import Constants.BASE_URL
import Constants.NETWORK_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mhs.weatherapp.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    fun provideNetworkTome() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    fun provideBodyInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideNoCacheInterceptor() = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Cache-Control", "no-cache")
            .header("Pragma", "no-cache")
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideClient(
        time: Long, body: HttpLoggingInterceptor, noCacheInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(body)
            .addInterceptor(noCacheInterceptor)
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): ApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
}