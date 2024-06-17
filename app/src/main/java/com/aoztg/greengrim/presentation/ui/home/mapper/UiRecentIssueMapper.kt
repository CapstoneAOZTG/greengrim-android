package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.RecentIssueListItem
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssueItem

fun RecentIssueListItem.toUiRecentIssueItem(
    onItemClickListener: (Long) -> Unit
) = UiRecentIssueItem(
    id = id,
    img = imgUrl,
    title = title,
    onItemClickListener
)