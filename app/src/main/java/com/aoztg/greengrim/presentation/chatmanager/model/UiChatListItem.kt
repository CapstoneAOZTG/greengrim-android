package com.aoztg.greengrim.presentation.chatmanager.model

data class UiChatListItem(
    val chatId: Long = -1L,
    val challengeId: Long = -1L,
    val titleImg: String = "",
    val title: String = "",
    val chatCount: Int = 0,
    val recentChat: String = "",
    val recentTime: String = "",
)