package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChatRoomsResponse
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatListItem


fun ChatRoomsResponse.toUiChatListItem(onItemClick: (String, Int, Int) -> Unit): UiChatListItem {
    return UiChatListItem(
        chatId = this.chatroomId,
        challengeId = this.challengeId,
        titleImg = this.imgUrl,
        title = this.title,
        creationDday = this.afterDay,
        onClickListener = onItemClick
    )
}