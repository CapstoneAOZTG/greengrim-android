package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.GrimDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiGrimDetail

internal fun GrimDetailResponse.toUiGrimDetail() = UiGrimDetail(
    imgUrl = grimInfo.imgUrl,
    title = grimInfo.title,
    profileImgUrl = grimInfo.memberSimpleInfo.profileImgUrl,
    nickName = grimInfo.memberSimpleInfo.nickName,
    date = createdAt
)