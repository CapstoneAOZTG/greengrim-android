package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.IssueItem
import com.aoztg.greengrim.presentation.ui.home.model.UiHomeRecentIssue


fun IssueItem.toUiRecentIssue(
    onItemClickListener: (Long) -> Unit
): UiHomeRecentIssue = UiHomeRecentIssue(
    id = id,
    iconImg = iconImgUrl,
    title = title,
    itemClickListener = onItemClickListener
)