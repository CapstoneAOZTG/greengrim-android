package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.presentation.ui.chat.model.ChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.toCategoryText

internal fun CertificationDetailResponse.toChallengeSimpleInfo() = ChallengeSimpleInfo(
    title = challengeInfo.title,
    category = challengeInfo.category.toCategoryText(),
    ticketCount = challengeInfo.ticketCount,
    description = challengeInfo.description
)