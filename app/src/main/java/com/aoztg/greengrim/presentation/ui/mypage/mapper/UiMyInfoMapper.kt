package com.aoztg.greengrim.presentation.ui.mypage.mapper

import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.presentation.ui.formatNumberWithCommas
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyInfo

internal fun MyInfoResponse.toUiMyInfo() = UiMyInfo(
    id = memberInfo.id,
    nickName = "${memberInfo.nickName} 님",
    profileImgUrl = memberInfo.profileImgUrl,
    introduction = memberInfo.introduction,
    email = email,
    myPoint = point.formatNumberWithCommas() + "GP",
)