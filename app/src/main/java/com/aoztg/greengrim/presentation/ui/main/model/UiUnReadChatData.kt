package com.aoztg.greengrim.presentation.ui.main.model

data class UiUnReadChatData(
    val chatId: Int = -1,
    val recentChat: String = "",
    val unReadCount: Int = 0,
    val recentChatTime: String = ""
)
