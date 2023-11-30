package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.ChatDao
import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.ChatAPI
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatAPI,
    private val chatDao: ChatDao
) : ChatRepository {

    override suspend fun enterChat(challengeId: Int): BaseState<EnterChatResponse> =
        runRemote(api.enterChat(challengeId))

    override suspend fun getChatRooms(): BaseState<List<ChatRoomsResponse>> =
        runRemote(api.getChatList())

    override suspend fun addChat(chatList: List<ChatEntity>): BaseState<Unit> {
        return try {
            chatList.forEach {
                chatDao.addChat(it)
            }
            BaseState.Success(Unit)
        } catch (e: Exception) {
            BaseState.Error("데이터 저장 실패", "FAIL")
        }
    }

    override suspend fun deleteChat(chatId: Int): BaseState<Unit> {
        return try {
            chatDao.deleteChat(chatId)
            BaseState.Success(Unit)
        } catch (e: Exception) {
            BaseState.Error("데이터 삭제 실패", "FAIL")
        }
    }

    override suspend fun getChat(chatId: Int, page: Int): BaseState<List<ChatEntity>> {
        return try {
            BaseState.Success(chatDao.getChat(chatId, page))
        } catch (e: Exception) {
            BaseState.Error("데이터 로딩 실패", "FAIL")
        }
    }

}