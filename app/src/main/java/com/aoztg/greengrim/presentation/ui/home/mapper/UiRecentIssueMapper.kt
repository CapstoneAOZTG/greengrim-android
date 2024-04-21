package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.IssueItem
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssues


fun IssueItem.toUiRecentIssue(
    onItemClickListener: (String) -> Unit
): UiRecentIssues = UiRecentIssues(
    iconImg = imgUrl,
    title = title,
    url = url,
    itemClickListener = onItemClickListener
)