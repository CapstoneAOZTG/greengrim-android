package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.data.remote.ChallengeAPI
import com.aoztg.greengrim.data.remote.CertificationAPI
import com.aoztg.greengrim.data.remote.ChatAPI
import com.aoztg.greengrim.data.remote.HomeAPI
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

    @Singleton
    @Provides
    fun provideChallengeService(retrofit: Retrofit): ChallengeAPI {
        return retrofit.create(ChallengeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeAPI {
        return retrofit.create(HomeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCertificationService(retrofit: Retrofit): CertificationAPI {
        return retrofit.create(CertificationAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideChatService(retrofit: Retrofit): ChatAPI {
        return retrofit.create(ChatAPI::class.java)
    }

}