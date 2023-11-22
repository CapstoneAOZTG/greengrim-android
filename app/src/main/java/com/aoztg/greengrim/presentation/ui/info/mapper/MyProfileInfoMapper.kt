package com.aoztg.greengrim.presentation.ui.info.mapper

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.presentation.ui.info.model.MyProfileInfo

internal fun GetProfileResponse.toMyProfileInfo() = MyProfileInfo(
    id = id,
    nickName = "$nickName 님",
    profileImgUrl = profileImgUrl,
    introduction = introduction
)