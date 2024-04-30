package com.aoztg.greengrim.presentation.ui.chat.model

data class UiChatMessage(
    val type: Int = -1,
    val senderId: Long = -1L,
    val message: String = "",
    val nickName: String = "",
    val sentDate: String = "",
    var sentTime: String = "",
    var profileVisibility: Boolean = true,
    val profileImg: String = "",
    val certId: Long = -1,
    val certImg: String = "",
    val createdAt: String = "",
    val onCertClickListener: (Long) -> Unit
)


