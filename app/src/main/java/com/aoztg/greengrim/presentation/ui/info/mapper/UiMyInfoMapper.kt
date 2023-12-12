package com.aoztg.greengrim.presentation.ui.info.mapper

import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.presentation.ui.info.model.UiMyInfo

internal fun MyInfoResponse.toUiMyInfo() = UiMyInfo(
    id = memberInfo.id,
    nickName = "${memberInfo.nickName} 님",
    profileImgUrl = memberInfo.profileImgUrl,
    introduction = memberInfo.introduction,
    email = email,
    myPoint = point.toString() + "P",
    hasWallet = wallet
)