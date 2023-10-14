package com.aoztg.greengrim.di

import com.aoztg.greengrim.repository.LoginRepository
import com.aoztg.greengrim.repository.LoginRepositoryImpl
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
    abstract fun bindLoginRepository(
        loginRepositoryImpl : LoginRepositoryImpl
    ) : LoginRepository
}