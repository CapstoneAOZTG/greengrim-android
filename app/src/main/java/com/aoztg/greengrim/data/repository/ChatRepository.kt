package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import retrofit2.Response

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Int
    ): Response<EnterChatResponse>

    suspend fun getChatRooms(): Response<List<ChatRoomsResponse>>
}