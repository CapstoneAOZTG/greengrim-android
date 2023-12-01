package com.aoztg.greengrim.presentation.chatmanager.mapper

import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.util.Constants.ENTER_AND_EXIT
import com.aoztg.greengrim.presentation.util.Constants.MY_CHAT
import com.aoztg.greengrim.presentation.util.Constants.NOTHING
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT


internal fun ChatMessage.toChatEntity(memberId: Long): ChatEntity {
    return ChatEntity(
        chatId = roomId,
        type = when (type) {
            "TALK" -> {
                if (memberId == senderId) MY_CHAT
                else OTHER_CHAT
            }

            "ENTER", "EXIT" -> {
                if (memberId == senderId) NOTHING
                else ENTER_AND_EXIT
            }

            else -> NOTHING
        },
        message = message,
        nickName = nickName,
        sentDate = sentDate,
        sentTime = sentTime,
        profileImg = profileImg,
        certId = certId,
        certImg = certImg
    )
}