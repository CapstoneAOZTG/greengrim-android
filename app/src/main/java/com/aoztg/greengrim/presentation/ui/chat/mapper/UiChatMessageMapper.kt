package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChatMessageItem
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.ENTER_AND_EXIT
import com.aoztg.greengrim.presentation.util.Constants.NOTHING
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT


internal fun ChatMessage.toUiChatMessage(
    memberId: Long,
    onCertClickListener: (Long) -> Unit,
    onProfileClickListener: (Long) -> Unit
): UiChatMessage {

    fun empty(empty: Long) {}
    return UiChatMessage(
        senderId = senderId,
        type = when (type) {
            "TALK", "CERT" -> {
                if (memberId == senderId) Constants.MY_CHAT
                else OTHER_CHAT
            }

            "ENTER", "EXIT" -> {
                ENTER_AND_EXIT
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
        createdAt = createdAt,
        onCertClickListener = if (certId == -1L) {
            ::empty
        } else {
            onCertClickListener
        },
        onProfileClickListener = if(senderId == -1L){
            ::empty
        } else {
            onProfileClickListener
        }
    )
}

internal fun ChatMessageItem.toUiChatMessageItem(
    memberId: Long,
    onCertClickListener: (Long) -> Unit,
    onProfileClickListener: (Long) -> Unit
): UiChatMessage = UiChatMessage(
    senderId = senderId,
    type = when (type) {
        "TALK", "CERT" -> {
            if (memberId == senderId) Constants.MY_CHAT
            else OTHER_CHAT
        }

        "ENTER", "EXIT" -> {
            ENTER_AND_EXIT
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
    createdAt = createdAt,
    onCertClickListener = onCertClickListener,
    onProfileClickListener = onProfileClickListener
)

//internal fun List<ChatMessageItem>.toUiChatMessageList(
//    memberId: Long,
//    onCertClickListener: (Long) -> Unit
//): List<UiChatMessage> {
//
//    val list = map {
//        it.toUiChatMessageItem(memberId, onCertClickListener)
//    }.toMutableList()
//
//    // DATE 집어넣고, 분까지 같은 메세지는 프로필, 닉네임 생략하는 로직. 시간은 맨 아래 메세지에만 삽입
//    val newList = mutableListOf<UiChatMessage>()
//
//    for (i in list.indices) {
//        if (i + 1 < list.size) {
//            val laterChat = list[i]
//            val pastChat = list[i + 1]
//
//            if (laterChat.createdAt.isNotBlank() &&
//                laterChat.senderId == pastChat.senderId &&
//                laterChat.createdAt.slice(0..11) == pastChat.createdAt.slice(0..11)
//            ) {
//                pastChat.sentTime = ""
//                laterChat.profileVisibility = false
//            }
//
//            newList.add(laterChat)
//
//            if (laterChat.createdAt.isNotBlank() &&
//                laterChat.createdAt.slice(0..7) != pastChat.createdAt.slice(0..7)
//            ) {
//                newList.add(
//                    UiChatMessage(
//                        type = DATE,
//                        message = list[i].sentDate,
//                        onCertClickListener = onCertClickListener
//                    )
//                )
//            }
//        }
//    }
//
//    return newList
//}