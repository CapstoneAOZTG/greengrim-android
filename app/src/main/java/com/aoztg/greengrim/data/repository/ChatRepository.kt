package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.ChatListDataRequest
import com.aoztg.greengrim.data.model.response.ChatInfoResponse
import com.aoztg.greengrim.data.model.response.ChatListDataResponse
import com.aoztg.greengrim.data.model.response.ChatMessageItem
import com.aoztg.greengrim.data.model.response.EnterChatResponse

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Long
    ): BaseState<EnterChatResponse>

    suspend fun getChatListData(
        body : List<ChatListDataRequest>
    ): BaseState<List<ChatListDataResponse>>

    suspend fun getUnReadChatData(): BaseState<List<UnReadChatEntity>>

    suspend fun addUnReadChatData(unReadChatEntity: UnReadChatEntity): BaseState<Unit>

    suspend fun deleteUnReadChatData(chatId: Long): BaseState<Unit>

    suspend fun exitChatRoom(id: Long): BaseState<Unit>

    suspend fun getChatMessage(
        roomId: Long,
        createdAt: String
    ): BaseState<List<ChatMessageItem>>

    suspend fun getChatInfo(
        id : Long
    ): BaseState<ChatInfoResponse>
}