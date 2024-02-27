package com.aoztg.greengrim.presentation.ui.challenge.mapper

import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom


fun ChallengeListResponse.toUiChallengeList(onItemClicked: (Long) -> Unit): UiChallengeList{
    return UiChallengeList(
        hasNext = hasNext,
        page = page,
        result = result.map {
            UiChallengeRoom(
                id = it.challengeInfo.id,
                imgUrl = it.challengeInfo.imgUrl,
                title = it.challengeInfo.title,
                keywords = it.challengeSimpleTags,
                onItemClicked = onItemClicked
            )
        }
    )
}