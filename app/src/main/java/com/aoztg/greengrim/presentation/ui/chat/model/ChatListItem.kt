package com.aoztg.greengrim.presentation.ui.chat.model

data class ChatListItem(
    val id: Int,
    val titleImg: String,
    val title: String,
    val recentChat: String="임시",
    val recentTime: String="오전 5:31",
    val creationDday: String,
    val onClickListener: (Int) -> Unit
)