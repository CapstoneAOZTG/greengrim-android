package com.aoztg.greengrim.presentation.ui.challenge.mapper

import com.aoztg.greengrim.data.model.ChallengeListResponse
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeListData
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom


fun ChallengeListResponse.toChallengeListData(onItemClicked: (String) -> Unit): ChallengeListData{
    return ChallengeListData(
        hasNext = hasNext,
        page = page,
        result = result.map {
            ChallengeRoom(
                id = it.challengeInfo.id,
                imgUrl = it.challengeInfo.imgUrl,
                title = it.challengeInfo.title,
                keywords = it.challengeSimpleTags,
                onItemClicked = onItemClicked
            )
        }
    )
}