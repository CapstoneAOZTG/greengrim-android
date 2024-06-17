package com.aoztg.greengrim.presentation.ui.mypage.mapper

import com.aoztg.greengrim.data.model.response.AnnounceListItem
import com.aoztg.greengrim.presentation.ui.mypage.model.UiAnnounceData


fun AnnounceListItem.toUiAnnounceData(
    onClickListener: (Long) -> Unit
) = UiAnnounceData(
    id = id,
    title = title,
    createdAt = createdAt,
    onClickListener = onClickListener
)