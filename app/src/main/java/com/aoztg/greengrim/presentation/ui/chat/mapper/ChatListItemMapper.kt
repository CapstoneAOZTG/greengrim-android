package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.ChatRoomsResponse
import com.aoztg.greengrim.presentation.ui.chat.model.ChatListItem


fun ChatRoomsResponse.toChatListItem(onItemClick: (Int) -> Unit): ChatListItem {
    return ChatListItem(
        id = this.id,
        titleImg = this.imgUrl,
        title = this.title,
        creationDday = this.afterDay,
        onClickListener = onItemClick
    )
}