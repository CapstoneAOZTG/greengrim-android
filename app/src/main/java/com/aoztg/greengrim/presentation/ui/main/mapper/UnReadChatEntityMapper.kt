package com.aoztg.greengrim.presentation.ui.main.mapper

import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.presentation.ui.main.model.UiUnReadChatData


internal fun UiUnReadChatData.toUnReadChatEntity() = UnReadChatEntity(
    chatId, unReadCount, recentChat, recentChatTime
)