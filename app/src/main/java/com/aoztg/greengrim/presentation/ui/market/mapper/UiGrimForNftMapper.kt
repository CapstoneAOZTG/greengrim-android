package com.aoztg.greengrim.presentation.ui.market.mapper

import com.aoztg.greengrim.data.model.response.MyGrimSimpleData
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimForNft


internal fun MyGrimSimpleData.toUiGrimForNft(
    itemSelectListener: (Int) -> Unit
) = UiGrimForNft(
    image = imgUrl,
    title = title,
    selectListener = itemSelectListener
)