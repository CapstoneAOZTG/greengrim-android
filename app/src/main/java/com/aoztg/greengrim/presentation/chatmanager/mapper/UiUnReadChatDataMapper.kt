package com.aoztg.greengrim.presentation.chatmanager.mapper

import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.presentation.chatmanager.model.UiUnReadChatData

internal fun UnReadChatEntity.toUiUnReadChatData() = UiUnReadChatData(
    chatId, unReadCount, recentChat, recentChatTime
)