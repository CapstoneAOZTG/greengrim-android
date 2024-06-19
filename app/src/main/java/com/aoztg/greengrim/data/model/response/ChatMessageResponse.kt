package com.aoztg.greengrim.data.model.response


data class ChatMessageResponse(
    val messages: List<ChatMessageItem>,
    val hasNext: Boolean
)

data class ChatMessageItem(
    val certId: Long,
    val certImg: String,
    val createdAt: String,
    val message: String,
    val nickName: String,
    val profileImg: String,
    val roomId: Long,
    val senderId: Long,
    val sentDate: String,
    val sentTime: String,
    val type: String,
    val child: Boolean
)