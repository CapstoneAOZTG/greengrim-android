package com.aoztg.greengrim.app.di

import android.content.Context
import androidx.room.Room
import com.aoztg.greengrim.data.local.ChatDao
import com.aoztg.greengrim.data.local.GreenGrimDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private const val DATABASE_NAME = "greengrim.db"

    @Provides
    @Singleton
    fun provideGreenGrimDatabase(@ApplicationContext context: Context): GreenGrimDatabase {
        return Room.databaseBuilder(context, GreenGrimDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: GreenGrimDatabase): ChatDao {
        return database.chatDao()
    }
}