package com.aoztg.greengrim.presentation.ui.main.model

data class UiUnReadChatData(
    val chatId: Int = -1,
    val unReadCount: Int = 0,
    val recentChat: String = "",
    val recentChatTime: String = ""
)
