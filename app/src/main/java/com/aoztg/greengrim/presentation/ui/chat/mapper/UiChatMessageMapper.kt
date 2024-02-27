package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChatMessageItem
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.ENTER_AND_EXIT
import com.aoztg.greengrim.presentation.util.Constants.NOTHING
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT


internal fun ChatMessage.toUiChatMessage(memberId: Long, onCertClickListener: (Long) -> Unit) :UiChatMessage{

    fun empty(empty: Long){}
    return UiChatMessage(
        type = when (type) {
            "TALK", "CERT" -> {
                if (memberId == senderId) Constants.MY_CHAT
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
        certImg = certImg,
        onCertClickListener = if(certId == -1L) {
            ::empty
        } else {
            onCertClickListener
        }
    )
}

internal fun ChatMessageItem.toUiChatMessage(
    onCertClickListener: (Long) -> Unit,
    type: Int
) = UiChatMessage(
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