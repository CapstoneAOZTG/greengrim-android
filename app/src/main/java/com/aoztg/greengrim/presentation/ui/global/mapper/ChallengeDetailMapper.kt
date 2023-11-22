package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.ChallengeDetail


fun ChallengeDetailResponse.toChallengeDetail(): ChallengeDetail{
    return ChallengeDetail(
        id = this.challengeInfo.id,
        imgUrl = this.challengeInfo.imgUrl,
        title = this.challengeInfo.title,
        description = this.challengeInfo.description,
        keywords = this.challengeTags,
        date = this.createdAt,
        entered = this.entered
    )
}