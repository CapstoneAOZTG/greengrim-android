package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.presentation.ui.main.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage


internal fun ChatMessage.toUiChatMessage(msgType: Int, onCertClickListener: (Int) -> Unit) = UiChatMessage(
    type = msgType,
    message = message,
    nickName = nickName,
    sentDate = sentDate,
    sentTime = sentTime,
    profileImg = profileImg,
    certId = certId,
    certImg = certImg,
    onCertClickListener = onCertClickListener
)