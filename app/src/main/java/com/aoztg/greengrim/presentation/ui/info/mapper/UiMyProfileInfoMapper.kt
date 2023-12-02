package com.aoztg.greengrim.presentation.ui.info.mapper

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.presentation.ui.info.model.UiMyProfileInfo

internal fun GetProfileResponse.toUiMyProfileInfo() = UiMyProfileInfo(
    id = id,
    nickName = "$nickName 님",
    profileImgUrl = profileImgUrl,
    introduction = introduction
)