package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.data.model.response.EnterChatResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatAPI {

    @POST("visitor/challenges/enter")
    suspend fun enterChat(
        @Query("id") challengeId: Int
    ): Response<EnterChatResponse>

    @GET("visitor/challenges/chatrooms")
    suspend fun getChatList(): Response<List<ChatRoomsResponse>>
}