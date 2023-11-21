package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.ChatIdDao
import com.aoztg.greengrim.data.local.ChatIdEntity
import com.aoztg.greengrim.data.model.ChatRoomsResponse
import com.aoztg.greengrim.data.model.EnterChatResponse
import com.aoztg.greengrim.data.remote.ChatAPI
import retrofit2.Response
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatAPI,
    private val chatIdDao: ChatIdDao
): ChatRepository {

    override suspend fun enterChat(challengeId: Int): Response<EnterChatResponse> = api.enterChat(challengeId)
    override suspend fun getChatRooms(): Response<List<ChatRoomsResponse>> = api.getChatList()
    override suspend fun addChatId(chatIdEntity: ChatIdEntity): Unit = chatIdDao.addChatId(chatIdEntity)
    override suspend fun getAllChatId(): List<ChatIdEntity> = chatIdDao.getAllChatId()
    override suspend fun deleteChatId(chatId: Int): Unit = chatIdDao.deleteChatId(chatId)
}