package com.aoztg.greengrim.presentation.ui.nft.mapper

import com.aoztg.greengrim.data.model.response.MyGrimSimpleData
import com.aoztg.greengrim.presentation.ui.nft.model.UiGrimForNft


internal fun MyGrimSimpleData.toUiGrimForNft(
    itemSelectListener: (Long, String) -> Unit
) = UiGrimForNft(
    id = id,
    image = imgUrl,
    title = title,
    selectListener = itemSelectListener
)