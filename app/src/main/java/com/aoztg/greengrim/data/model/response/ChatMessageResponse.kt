package com.aoztg.greengrim.data.model.response

data class ChatMessageResponse(
    val hasNext: Boolean,
    val page: Int,
    val result: List<ChatMessageItem>
)

data class ChatMessageItem(
    val certId: Long,
    val certImg: String,
    val createdAt: Int,
    val message: String,
    val nickName: String,
    val profileImg: String,
    val roomId: Long,
    val senderId: Long,
    val sentDate: String,
    val sentTime: String,
    val type: String
)