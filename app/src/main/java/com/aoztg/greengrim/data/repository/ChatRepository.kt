package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.EnterChatResponse
import retrofit2.Response

interface ChatRepository {

    suspend fun enterChat(
        challengeId: Int
    ): Response<EnterChatResponse>
}