package com.aoztg.greengrim.presentation.chatmanager.mapper

import com.aoztg.greengrim.data.model.response.ChatListDataResponse
import com.aoztg.greengrim.presentation.chatmanager.model.UiChatListItem


fun ChatListDataResponse.toUiChatListItem() =
    UiChatListItem(
        chatId = chatroomInfo.chatroomId,
        challengeId = id,
        titleImg = imgUrl,
        title = title,
        chatCount = chatroomInfo.newMessageCount,
        recentChat = chatroomInfo.lastMessageContent,
        recentTime = chatroomInfo.lastMessageTime
    )
