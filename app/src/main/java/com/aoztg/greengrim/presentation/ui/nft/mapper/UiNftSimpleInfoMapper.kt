package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.NftSimpleResponse
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftSimpleInfo


fun NftSimpleResponse.toUiNftSimpleInfo(): UiNftSimpleInfo = UiNftSimpleInfo(
    nftId = nftId,
    img = imgUrl,
    title = title,
    background = traitsInfo.background,
    hair = traitsInfo.hair,
    face = traitsInfo.face,
    gesture = traitsInfo.gesture,
    accessory = traitsInfo.gesture,
    shoes = traitsInfo.shoes,
    rarity = traitsInfo.rarity
)