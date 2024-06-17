package com.aoztg.greengrim.data.model.response

data class RecentIssueResponse(
    val issueInfos: List<IssueItem>
)

data class IssueItem(
    val id: Long,
    val title: String,
    val iconImgUrl: String
)
