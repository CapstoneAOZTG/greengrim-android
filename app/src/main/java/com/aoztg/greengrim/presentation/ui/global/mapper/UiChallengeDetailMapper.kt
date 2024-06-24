package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiChallengeDetail


fun ChallengeDetailResponse.toUiChallengeDetail(): UiChallengeDetail {
    return UiChallengeDetail(
        id = challengeInfo.id,
        imgUrl = challengeInfo.imgUrl,
        title = challengeInfo.title,
        description = challengeInfo.description,
        keywords = challengeTags,
        date = createdAt,
        chatRoomId = chatroomId,
        entered = entered,
        mine = mine
    )
}