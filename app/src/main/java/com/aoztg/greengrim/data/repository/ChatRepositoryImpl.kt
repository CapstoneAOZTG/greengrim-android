package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.ChatRoomsResponse
import com.aoztg.greengrim.data.model.EnterChatResponse
import com.aoztg.greengrim.data.remote.ChatAPI
import retrofit2.Response
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val api: ChatAPI): ChatRepository {

    override suspend fun enterChat(challengeId: Int): Response<EnterChatResponse> = api.enterChat(challengeId)
    override suspend fun getChatRooms(): Response<List<ChatRoomsResponse>> = api.getChatList()
}