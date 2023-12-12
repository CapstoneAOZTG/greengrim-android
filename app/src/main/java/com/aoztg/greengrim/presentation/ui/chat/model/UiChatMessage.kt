package com.aoztg.greengrim.presentation.ui.chat.model

data class UiChatMessage(
    val type: Int = -1,
    val message: String = "",
    val nickName: String = "",
    val sentDate: String = "",
    val sentTime: String = "",
    val profileImg: String = "",
    val certId: Int = -1,
    val certImg: String = "",
    val onCertClickListener: (Int) -> Unit
)
