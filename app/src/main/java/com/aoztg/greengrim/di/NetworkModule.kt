package com.aoztg.greengrim.di

import com.aoztg.greengrim.config.RetrofitInterceptor
import com.aoztg.greengrim.data.network.loginnetwork.LoginAPI
import com.aoztg.greengrim.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {

        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(RetrofitInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginService(retrofit : Retrofit) : LoginAPI {
        return retrofit.create(LoginAPI::class.java)
    }

}