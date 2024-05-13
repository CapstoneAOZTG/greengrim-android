package com.aoztg.greengrim.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.room.Room
import com.aoztg.greengrim.app.App.Companion.dataStore
import com.aoztg.greengrim.data.config.KeyDataStore
import com.aoztg.greengrim.data.config.KeyDataStoreManager
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

    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> = context.dataStore

    @Provides
    fun provideKeyDataStoreManager(dataStore: DataStore<Preferences>): KeyDataStoreManager =
        KeyDataStore(dataStore = dataStore)
}