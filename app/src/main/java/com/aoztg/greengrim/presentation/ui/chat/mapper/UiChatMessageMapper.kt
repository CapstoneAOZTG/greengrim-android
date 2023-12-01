package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage


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

internal fun ChatEntity.toUiChatMessage(onCertClickListener: (Int) -> Unit) = UiChatMessage(
    type = type,
    message = message,
    nickName = nickName,
    sentDate = sentDate,
    sentTime = sentTime,
    profileImg = profileImg,
    certId = certId,
    certImg = certImg,
    onCertClickListener = onCertClickListener
)