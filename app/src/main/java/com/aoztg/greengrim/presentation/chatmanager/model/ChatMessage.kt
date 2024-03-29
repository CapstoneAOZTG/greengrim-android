package com.aoztg.greengrim.presentation.chatmanager.model

data class ChatMessage(
    val type: String = "",
    val senderId: Long = -1,
    val roomId: Long = -1,
    val message: String = "",
    val nickName: String = "",
    val sentDate: String = "",
    val sentTime: String = "",
    val profileImg: String = "",
    val certId: Long = -1,
    val certImg: String = "",
)
