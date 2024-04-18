package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftDetailInfo

internal fun NftDetailResponse.toUiNftDetail() = UiNftDetailInfo(
    nftId = nftInfo.id,
    nftImage = nftInfo.imgUrl,
    title = nftInfo.title,
    description = nftInfo.description,
    profileImage = memberSimpleInfo.profileImgUrl,
    nickName = memberSimpleInfo.nickName,
    date = nftInfo.createdAt,
    background = traitsInfo.background,
    hair = traitsInfo.hair,
    face = traitsInfo.face,
    gesture = traitsInfo.gesture,
    accessory = traitsInfo.accessory,
    shoes = traitsInfo.shoes,
    liked = liked
)