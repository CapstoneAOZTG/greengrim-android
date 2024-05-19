package com.aoztg.greengrim.presentation.ui.chat.model

data class UiChatInfo(
    val category: String = "",
    val goalCount: String = "",
    val certificationCount: Int = 0,
    val participantCount : String = "",
    val todayCertification: Boolean = false
)
