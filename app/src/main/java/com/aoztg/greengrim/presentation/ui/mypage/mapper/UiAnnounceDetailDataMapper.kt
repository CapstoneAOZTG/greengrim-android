package com.aoztg.greengrim.presentation.ui.mypage.mapper

import com.aoztg.greengrim.data.model.response.AnnounceDetailResponse
import com.aoztg.greengrim.presentation.ui.mypage.model.UiAnnounceDetailData


fun AnnounceDetailResponse.toUiAnnounceDetailData() = UiAnnounceDetailData(
    title = title,
    content = content,
    createdAt = createdAt
)