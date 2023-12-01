package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.ChatDao
import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ChatEntityResponse
import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.ChatAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatAPI,
    private val chatDao: ChatDao
) : ChatRepository {

    override suspend fun enterChat(challengeId: Int): BaseState<EnterChatResponse> =
        runRemote(api.enterChat(challengeId))

    override suspend fun getChatRooms(): BaseState<List<ChatRoomsResponse>> =
        runRemote(api.getChatList())

    override suspend fun addChat(chatMessage: ChatEntity): BaseState<Unit> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.addChat(chatMessage)
            }.await()
            BaseState.Success(response)
        } catch (e: Exception) {
            BaseState.Error("데이터 저장 실패", "FAIL")
        }
    }

    override suspend fun deleteChat(chatId: Int): BaseState<Unit> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.deleteChat(chatId)
            }.await()
            BaseState.Success(response)
        } catch (e: Exception) {
            BaseState.Error("데이터 삭제 실패", "FAIL")
        }
    }

    override suspend fun getChat(chatId: Int, page: Int): BaseState<ChatEntityResponse> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.getChat(chatId, page)
            }.await()

            BaseState.Success(
                ChatEntityResponse(
                    hasNext = response.size >= 30  ,
                    chatEntityList = response.reversed()
                )
            )
        } catch (e: Exception) {
            BaseState.Error("데이터 로딩 실패", "FAIL")
        }

    }

}