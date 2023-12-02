package com.aoztg.greengrim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_message  WHERE chatId = :chatId ORDER BY idx DESC LIMIT 30 OFFSET :page * 30")
    fun getChat(chatId: Int, page: Int): List<ChatEntity>

    @Insert(onConflict = REPLACE)
    fun addChat(chatEntity: ChatEntity)

    @Query("DELETE FROM chat_message WHERE chatId = :chatId")
    fun deleteChat(chatId: Int)

    @Query("DELETE FROM unread_chat_data WHERE chatId = :chatId")
    fun deleteUnReadChatData(chatId: Int)

    @Insert(onConflict = REPLACE)
    fun addUnReadChatData(unReadChatEntity: UnReadChatEntity)

    @Query("SELECT * FROM unread_chat_data")
    fun getUnReadChatData(): List<UnReadChatEntity>
}