package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.NftCollectionDetailResponse
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionDetailInfo


fun NftCollectionDetailResponse.toUiNftCollectionDetailInfo() = UiNftCollectionDetailInfo(
    nftId = nftId,
    nftImage = imgUrl,
    title = title,
    background = traitsInfo.background,
    hair = traitsInfo.hair,
    face = traitsInfo.face,
    gesture = traitsInfo.gesture,
    accessory = traitsInfo.accessory,
    shoes = traitsInfo.shoes,
    rarity = traitsInfo.rarity
)