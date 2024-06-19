package com.aoztg.greengrim.data.repository

import android.util.Log
import com.aoztg.greengrim.data.local.ChatDao
import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.ChatListDataRequest
import com.aoztg.greengrim.data.model.response.ChatInfoResponse
import com.aoztg.greengrim.data.model.response.ChatListDataResponse
import com.aoztg.greengrim.data.model.response.ChatMessageItem
import com.aoztg.greengrim.data.model.response.ChatMessageResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.ChatAPI
import com.aoztg.greengrim.presentation.util.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatAPI,
    private val chatDao: ChatDao
) : ChatRepository {

    override suspend fun enterChat(challengeId: Long): BaseState<EnterChatResponse> =
        runRemote { api.enterChat(challengeId) }

    override suspend fun getChatListData(body: List<ChatListDataRequest>): BaseState<List<ChatListDataResponse>> =
        runRemote { api.getChatListData(body) }

    override suspend fun exitChatRoom(id: Long): BaseState<Unit> =
        runRemote { api.exitChatRoom(id) }

    override suspend fun addUnReadChatData(unReadChatEntity: UnReadChatEntity): BaseState<Unit> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.addUnReadChatData(unReadChatEntity)
            }.await()

            BaseState.Success(response)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            BaseState.Error("데이터 저장 실패", "FAIL")
        }
    }

    override suspend fun deleteUnReadChatData(chatId: Long): BaseState<Unit> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.deleteUnReadChatData(chatId)
            }.await()

            BaseState.Success(response)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            BaseState.Error("데이터 삭제 실패", "FAIL")
        }
    }

    override suspend fun getUnReadChatData(): BaseState<List<UnReadChatEntity>> {
        return try {
            val response = CoroutineScope(Dispatchers.IO).async {
                chatDao.getUnReadChatData()
            }.await()

            BaseState.Success(response)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            BaseState.Error("데이터 불러오기 실패", "FAIL")
        }
    }

    override suspend fun getChatMessage(
        roomId: Long,
        createdAt: String
    ): BaseState<ChatMessageResponse> =
        runRemote { api.getChatMessage(roomId, createdAt) }

    override suspend fun getChatInfo(id: Long): BaseState<ChatInfoResponse> =
        runRemote { api.getChatInfo(id) }

}