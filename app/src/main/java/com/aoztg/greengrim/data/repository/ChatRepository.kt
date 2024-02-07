package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ChatMessageResponse
import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Int
    ): BaseState<EnterChatResponse>

    suspend fun getChatRooms(): BaseState<List<ChatRoomsResponse>>

    suspend fun getUnReadChatData(): BaseState<List<UnReadChatEntity>>

    suspend fun addUnReadChatData(unReadChatEntity: UnReadChatEntity): BaseState<Unit>

    suspend fun deleteUnReadChatData(chatId: Int): BaseState<Unit>

    suspend fun exitChatRoom(id: Int): BaseState<Unit>

    suspend fun getChatMessage(
        roomId: Int,
        page: Int,
        size: Int
    ): BaseState<ChatMessageResponse>
}