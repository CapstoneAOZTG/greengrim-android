package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.HotChallengeInfo
import com.aoztg.greengrim.presentation.ui.home.model.UiHotChallenge


fun HotChallengeInfo.toUiHotChallenge(onItemClicked: (Long) -> Unit): UiHotChallenge {
    return UiHotChallenge(
        id = this.id,
        imgUrl = this.imgUrl,
        title = this.title,
        description = "\uD83D\uDD25 " + this.description,
        itemClickListener = onItemClicked
    )
}