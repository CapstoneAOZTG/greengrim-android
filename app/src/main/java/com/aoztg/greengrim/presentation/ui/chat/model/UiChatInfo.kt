package com.aoztg.greengrim.presentation.ui.chat.model

data class UiChatInfo(
    val category: String = "",
    val ticketCount: String = "",
    val goalCount: String = "",
    val certificationCount: Int = 0,
    val todayCertification: Boolean = false
)
