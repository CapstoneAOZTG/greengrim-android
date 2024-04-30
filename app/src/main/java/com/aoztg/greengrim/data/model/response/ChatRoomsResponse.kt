package com.aoztg.greengrim.data.model.response

data class ChatListDataResponse(
    val id: Long,
    val title: String,
    val chatroomInfo : ChatRoomInfo,
    val imgUrl: String
)

data class ChatRoomInfo(
    val chatroomId : Long,
    val lastMessageContent : String,
    val lastMessageTime : String,
    val newMessageCount : Int
)


