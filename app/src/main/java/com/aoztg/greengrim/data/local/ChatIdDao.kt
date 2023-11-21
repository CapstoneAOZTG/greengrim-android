package com.aoztg.greengrim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ChatIdDao {

    @Query("SELECT * FROM chat_id")
    fun getAllChatId(): List<ChatIdEntity>

    @Insert(onConflict = REPLACE)
    fun addChatId(chatIdEntity: ChatIdEntity)

    @Query("DELETE FROM chat_id WHERE chatId = :chatId")
    fun deleteChatId(chatId: Int)
}