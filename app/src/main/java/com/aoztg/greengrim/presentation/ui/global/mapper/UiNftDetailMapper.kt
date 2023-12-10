package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.NftState
import com.aoztg.greengrim.presentation.ui.global.model.UiNftDetail

internal fun NftDetailResponse.toUiNftDetail() = UiNftDetail(
    nftId = nftInfo.id,
    nftImage = nftInfo.imgUrl,
    title = nftInfo.title,
    description = nftInfo.description,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    date = nftInfo.createdAt,
    contractAddress = contracts.substring(0..5) + ".." + contracts.substring(contracts.length - 3 until contracts.length),
    tokenId = tokenId,
    price = if (price == "NOT SALE") price else "$price KLAY",
    btnState = if (marketed && !mine) NftState.CAN_BUY else if (!marketed && mine) NftState.CAN_SELL else NftState.NONE,
)