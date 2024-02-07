package com.aoztg.greengrim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ChatDao {

    @Query("DELETE FROM unread_chat_data WHERE chatId = :chatId")
    fun deleteUnReadChatData(chatId: Int)

    @Insert(onConflict = REPLACE)
    fun addUnReadChatData(unReadChatEntity: UnReadChatEntity)

    @Query("SELECT * FROM unread_chat_data")
    fun getUnReadChatData(): List<UnReadChatEntity>
}