package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChatInfoResponse
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatInfo
import com.aoztg.greengrim.presentation.ui.toCategoryText


internal fun ChatInfoResponse.toUiChatInfo() = UiChatInfo(
    category = category.toCategoryText(),
    ticketCount = ticketCount,
    goalCount = goalCount,
    certificationCount = certificationCount,
    todayCertification = todayCertification
)