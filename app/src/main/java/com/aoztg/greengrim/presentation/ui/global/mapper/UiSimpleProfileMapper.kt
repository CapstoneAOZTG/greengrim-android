package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiSimpleProfileData


fun GetProfileResponse.toUiSimpleProfile() : UiSimpleProfileData = UiSimpleProfileData(
    nickName = nickName,
    profileImgUrl = profileImgUrl,
    introduction = introduction
)