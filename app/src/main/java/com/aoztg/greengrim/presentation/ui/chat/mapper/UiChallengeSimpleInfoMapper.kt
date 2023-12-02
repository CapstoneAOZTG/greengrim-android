package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.presentation.ui.chat.model.UiChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.toCategoryText

internal fun ChallengeDetailResponse.toUiChallengeSimpleInfo() = UiChallengeSimpleInfo(
    title = challengeInfo.title,
    category = challengeTags.category.toCategoryText(),
    ticketCount = challengeTags.ticketCount,
    description = challengeInfo.description
)