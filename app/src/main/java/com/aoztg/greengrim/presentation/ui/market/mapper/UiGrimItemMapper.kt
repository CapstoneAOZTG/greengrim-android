package com.aoztg.greengrim.presentation.ui.market.mapper

import com.aoztg.greengrim.data.model.response.GrimListItem
import com.aoztg.greengrim.data.model.response.GrimListResponse
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimItem


internal fun GrimListItem.toUiGrimItem(
    itemClickListener: (Int) -> Unit
) = UiGrimItem(
    id = id,
    image = imgUrl,
    title = title,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    navigateToGrimDetail = itemClickListener
)