package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiChallengeDetail


fun ChallengeDetailResponse.toUiChallengeDetail(): UiChallengeDetail{
    return UiChallengeDetail(
        id = this.challengeInfo.id,
        imgUrl = this.challengeInfo.imgUrl,
        title = this.challengeInfo.title,
        description = this.challengeInfo.description,
        keywords = this.challengeTags,
        date = this.createdAt,
        entered = this.entered
    )
}