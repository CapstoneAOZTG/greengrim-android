package com.aoztg.greengrim.data.model.response

data class ChatRoomsResponse(
    val chatroomId: Int,
    val challengeId: Int,
    val title: String,
    val afterDay: String,
    val imgUrl: String
)
