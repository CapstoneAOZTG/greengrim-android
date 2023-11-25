package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.presentation.ui.chat.model.ChatListItem


fun ChatRoomsResponse.toChatListItem(onItemClick: (Int, Int) -> Unit): ChatListItem {
    return ChatListItem(
        chatId = this.chatroomId,
        challengeId = this.challengeId,
        titleImg = this.imgUrl,
        title = this.title,
        creationDday = this.afterDay,
        onClickListener = onItemClick
    )
}