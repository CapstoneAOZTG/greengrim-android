package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.GrimListItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiGrimItem


internal fun GrimListItem.toUiGrimItem(
    itemClickListener: (Long) -> Unit
) = UiGrimItem(
    id = id,
    image = imgUrl,
    title = title,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    navigateToGrimDetail = itemClickListener
)