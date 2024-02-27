package com.aoztg.greengrim.presentation.ui.chat.model

data class UiChatListItem(
    val chatId: Long,
    val challengeId: Long,
    val titleImg: String,
    val title: String,
    val chatCount: Int = 0,
    val recentChat: String = "",
    val recentTime: String = "",
    val creationDday: String,
    val onClickListener: (String, Long, Long) -> Unit
)