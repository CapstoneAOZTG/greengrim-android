package com.aoztg.greengrim.presentation.ui.chat.model

data class ChatListItem(
    val id:String,
    val titleImg: String,
    val title: String,
    val recentChat: String,
    val recentTime: String,
    val creationDday: String,
    val onClickListener: (String) -> Unit
)