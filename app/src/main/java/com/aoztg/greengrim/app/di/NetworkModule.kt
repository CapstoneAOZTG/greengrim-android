package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.data.config.AccessTokenInterceptor
import com.aoztg.greengrim.data.config.BearerInterceptor
import com.aoztg.greengrim.data.config.KeyDataStoreManager
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

    @Provides
    fun provideAccessTokenInterceptor(keyDataStoreManager: KeyDataStoreManager): AccessTokenInterceptor =
        AccessTokenInterceptor(keyDataStoreManager)

    @Provides
    fun provideBearerInterceptor(keyDataStoreManager: KeyDataStoreManager): BearerInterceptor =
        BearerInterceptor(keyDataStoreManager)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_DEV_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        bearerInterceptor: BearerInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(accessTokenInterceptor)
            .addInterceptor(bearerInterceptor)
            .build()
    }

}