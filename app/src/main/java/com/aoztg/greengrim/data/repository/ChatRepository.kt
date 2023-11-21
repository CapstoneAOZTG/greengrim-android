package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.ChatIdEntity
import com.aoztg.greengrim.data.model.ChatRoomsResponse
import com.aoztg.greengrim.data.model.EnterChatResponse
import retrofit2.Response

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Int
    ): Response<EnterChatResponse>

    suspend fun getChatRooms(): Response<List<ChatRoomsResponse>>

    suspend fun getAllChatId(): List<ChatIdEntity>

    suspend fun addChatId(chatIdEntity: ChatIdEntity): Unit

    suspend fun deleteChatId(chatId: Int): Unit
}