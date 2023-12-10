package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.InfoBeforeSellNftResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiNftSellData

internal fun InfoBeforeSellNftResponse.toUiNftSellData() = UiNftSellData(
    image = nftSimpleInfo.imgUrl,
    title = nftSimpleInfo.title,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName
)