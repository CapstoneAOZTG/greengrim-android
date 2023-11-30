package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import retrofit2.Response

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Int
    ): BaseState<EnterChatResponse>

    suspend fun getChatRooms(): BaseState<List<ChatRoomsResponse>>
}