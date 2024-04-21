package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.HotNftItem
import com.aoztg.greengrim.presentation.ui.home.model.UiHotNftItem

fun HotNftItem.toUiHotNftItem(
    onItemClickListener : (Long) -> Unit
): UiHotNftItem = UiHotNftItem(
    id = nftSimpleInfo.id,
    image = nftSimpleInfo.imgUrl,
    title = nftSimpleInfo.title,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    navigateToNftDetail = onItemClickListener,
    likeCount = likeCount
)

