package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.NftListItem
import com.aoztg.greengrim.presentation.ui.market.model.UiNftItem

internal fun NftListItem.toUiNftItem(onItemClickListener: (Int) -> Unit) = UiNftItem(
    id = nftAndMemberInfo.nftSimpleInfo.id,
    image = nftAndMemberInfo.nftSimpleInfo.imgUrl,
    title = nftAndMemberInfo.nftSimpleInfo.title,
    profileImage = nftAndMemberInfo.memberSimpleInfo.profileImgUrl,
    nickName = nftAndMemberInfo.memberSimpleInfo.nickName,
    price = if (price == "NOT SALE") price else "$price KLAY",
    navigateToNftDetail = onItemClickListener
)