package com.aoztg.greengrim.data.model.response

data class RecentIssueResponse(
    val issueInfos: List<IssueItem>
)

data class IssueItem(
    val title: String,
    val imgUrl: String,
    val url: String
)
