package com.aoztg.greengrim.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UnReadChatEntity::class], version = 1)
abstract class GreenGrimDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao
}