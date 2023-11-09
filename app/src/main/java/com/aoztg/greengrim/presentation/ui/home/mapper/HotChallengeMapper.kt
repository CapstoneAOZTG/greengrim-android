package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.HotChallengeInfo
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge


fun HotChallengeInfo.toHotChallenge(): HotChallenge {
    return HotChallenge(
        imgUrl = this.challengeInfo.imgUrl,
        title = this.challengeInfo.title,
        keywords = this.hotChallengeTags
    )
}