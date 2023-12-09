package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiNftDetail

internal fun NftDetailResponse.toUiNftDetail() = UiNftDetail(
    nftId = nftInfo.id,
    nftImage = nftInfo.imgUrl,
    title = nftInfo.title,
    description = nftInfo.description,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    date = nftInfo.createdAt,
    contractAddress = contracts,
    tokenId = tokenId,
    price = price,
    btnState = if(marketed && !mine) "CAN_BUY" else if(!marketed && mine) "CAN_SELL" else "NONE",
)