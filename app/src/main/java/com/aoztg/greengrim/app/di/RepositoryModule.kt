package com.aoztg.greengrim.app.di

import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.data.repository.ImageRepositoryImpl
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
}