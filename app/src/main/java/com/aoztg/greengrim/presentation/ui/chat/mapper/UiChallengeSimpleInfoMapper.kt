package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.presentation.ui.chat.model.UiChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.toCategoryText

internal fun CertificationDetailResponse.toUiChallengeSimpleInfo() = UiChallengeSimpleInfo(
    title = challengeInfo.title,
    category = challengeInfo.category.toCategoryText(),
    ticketCount = challengeInfo.ticketCount,
    description = challengeInfo.description
)