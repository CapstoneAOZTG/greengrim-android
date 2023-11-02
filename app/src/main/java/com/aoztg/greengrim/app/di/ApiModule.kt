package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.data.remote.ImageAPI
import com.aoztg.greengrim.data.remote.InfoAPI
import com.aoztg.greengrim.data.remote.IntroAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideIntroService(retrofit: Retrofit): IntroAPI {
        return retrofit.create(IntroAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideImageService(retrofit: Retrofit): ImageAPI {
        return retrofit.create(ImageAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideInfoService(retrofit: Retrofit): InfoAPI {
        return retrofit.create(InfoAPI::class.java)
    }

}