package com.aoztg.greengrim.presentation.ui.chat.model

data class ChatMessage(
    val profileImg: String = "",
    val nick: String = "",
    val img: String = "",
    val message: String,
    val type: Int
)
