package com.aoztg.greengrim.data.model.response

data class ChatRoomsResponse(
    val chatroomId: Long,
    val challengeId: Long,
    val title: String,
    val afterDay: String,
    val imgUrl: String
)
