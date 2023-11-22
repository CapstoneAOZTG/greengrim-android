package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.ChatDao
import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import com.aoztg.greengrim.data.remote.ChatAPI
import retrofit2.Response
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatAPI,
    private val chatDao: ChatDao
): ChatRepository {

    override suspend fun enterChat(challengeId: Int): Response<EnterChatResponse> = api.enterChat(challengeId)
    override suspend fun getChatRooms(): Response<List<ChatRoomsResponse>> = api.getChatList()
}