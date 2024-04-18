package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.NftCollectionItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionItem


fun NftCollectionItem.toUiNftCollectionItem() : UiNftCollectionItem = UiNftCollectionItem(
    id = id,
    image = imgUrl,
    title = title,
    number = tokenId
)