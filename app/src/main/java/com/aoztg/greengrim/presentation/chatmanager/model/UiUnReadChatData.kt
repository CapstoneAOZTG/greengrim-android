package com.aoztg.greengrim.presentation.chatmanager.model

data class UiUnReadChatData(
    val chatId: Long = -1,
    val unReadCount: Int = 0,
    val recentChat: String = "",
    val recentChatTime: String = "",
    val recentChatDate: String = ""
)
