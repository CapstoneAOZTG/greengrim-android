package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.data.repository.AttendCheckRepository
import com.aoztg.greengrim.data.repository.AttendCheckRepositoryImpl
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.CertificationRepositoryImpl
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChallengeRepositoryImpl
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.data.repository.ChatRepositoryImpl
import com.aoztg.greengrim.data.repository.HomeRepository
import com.aoztg.greengrim.data.repository.HomeRepositoryImpl
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.data.repository.ImageRepositoryImpl
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.data.repository.InfoRepositoryImpl
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.data.repository.IntroRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindIntroRepository(
        loginRepositoryImpl: IntroRepositoryImpl
    ): IntroRepository

    @Singleton
    @Binds
    abstract fun bindImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository

    @Singleton
    @Binds
    abstract fun bindInfoRepository(
        infoRepositoryImpl: InfoRepositoryImpl
    ): InfoRepository

    @Singleton
    @Binds
    abstract fun bindChallengeRepository(
        challengeRepositoryImpl: ChallengeRepositoryImpl
    ): ChallengeRepository

    @Singleton
    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Singleton
    @Binds
    abstract fun bindCertificationRepository(
        certificationRepositoryImpl: CertificationRepositoryImpl
    ): CertificationRepository

    @Singleton
    @Binds
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Singleton
    @Binds
    abstract fun bindAttendCheckRepository(
        attendCheckRepositoryImpl: AttendCheckRepositoryImpl
    ): AttendCheckRepository
}