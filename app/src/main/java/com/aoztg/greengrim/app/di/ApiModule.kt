package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.data.remote.AttendCheckAPI
import com.aoztg.greengrim.data.remote.CertificationAPI
import com.aoztg.greengrim.data.remote.ChallengeAPI
import com.aoztg.greengrim.data.remote.ChatAPI
import com.aoztg.greengrim.data.remote.ImageAPI
import com.aoztg.greengrim.data.remote.MemberAPI
import com.aoztg.greengrim.data.remote.NftAPI
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
    fun provideImageService(retrofit: Retrofit): ImageAPI {
        return retrofit.create(ImageAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideInfoService(retrofit: Retrofit): MemberAPI {
        return retrofit.create(MemberAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideChallengeService(retrofit: Retrofit): ChallengeAPI {
        return retrofit.create(ChallengeAPI::class.java)
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

    @Singleton
    @Provides
    fun provideAttendCheckService(retrofit: Retrofit): AttendCheckAPI {
        return retrofit.create(AttendCheckAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNftService(retrofit: Retrofit): NftAPI {
        return retrofit.create(NftAPI::class.java)
    }

}