package com.aoztg.greengrim.presentation.ui.market.mapper

import com.aoztg.greengrim.data.model.response.MyGrimSimpleData
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimForNft


internal fun MyGrimSimpleData.toUiGrimForNft(
    itemSelectListener: (Int, String) -> Unit
) = UiGrimForNft(
    id = id,
    image = imgUrl,
    title = title,
    selectListener = itemSelectListener
)