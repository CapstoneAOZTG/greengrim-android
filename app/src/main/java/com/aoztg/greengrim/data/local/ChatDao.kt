package com.aoztg.greengrim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_message WHERE chatId = :chatId")
    fun getChat(chatId: Int): List<ChatEntity>

    @Insert(onConflict = REPLACE)
    fun addChat(chatEntity: ChatEntity)

    @Query("DELETE FROM chat_message WHERE chatId = :chatId")
    fun deleteChat(chatId: Int)
}