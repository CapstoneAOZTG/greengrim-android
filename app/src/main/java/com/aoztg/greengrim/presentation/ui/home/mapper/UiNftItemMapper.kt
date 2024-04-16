package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.NftListItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem

internal fun NftListItem.toUiNftItem(onItemClickListener: (Long) -> Unit) = UiNftItem(
    id = nftSimpleInfo.id,
    image = nftSimpleInfo.imgUrl,
    title = nftSimpleInfo.title,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    navigateToNftDetail = onItemClickListener
)