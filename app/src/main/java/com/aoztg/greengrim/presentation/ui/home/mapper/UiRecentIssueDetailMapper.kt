package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.RecentIssueDetailResponse
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssueDetail


fun RecentIssueDetailResponse.toUiRecentIssueDetail() = UiRecentIssueDetail(
    title = title,
    description = content,
    createdAt = createdAt,
    imgList = imgUrls
)