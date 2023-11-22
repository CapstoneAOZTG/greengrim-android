package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.HotChallengeInfo
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge


fun HotChallengeInfo.toHotChallenge(onItemClicked: (Int) -> Unit): HotChallenge {
    return HotChallenge(
        id = this.challengeInfo.id,
        imgUrl = this.challengeInfo.imgUrl,
        title = this.challengeInfo.title,
        keywords = this.hotChallengeTags,
        itemClickListener = onItemClicked
    )
}