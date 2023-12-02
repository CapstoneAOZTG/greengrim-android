package com.aoztg.greengrim.presentation.chatmanager.mapper

import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.presentation.chatmanager.model.UiUnReadChatData


internal fun UiUnReadChatData.toUnReadChatEntity() = UnReadChatEntity(
    chatId, unReadCount, recentChat, recentChatTime
)