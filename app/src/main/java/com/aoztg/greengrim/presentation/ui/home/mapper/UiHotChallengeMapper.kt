package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.HotChallengeInfo
import com.aoztg.greengrim.presentation.ui.home.model.UiHotChallenge


fun HotChallengeInfo.toUiHotChallenge(onItemClicked: (Long) -> Unit): UiHotChallenge {
    return UiHotChallenge(
        id = this.challengeInfo.id,
        imgUrl = this.challengeInfo.imgUrl,
        title = this.challengeInfo.title,
        keywords = this.hotChallengeTags,
        itemClickListener = onItemClicked
    )
}