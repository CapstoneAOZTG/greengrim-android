package com.aoztg.greengrim.data.model.response

data class RecentIssueListResponse(
    val page: Int,
    val hasNext: Boolean,
    val result : List<RecentIssueListItem>
)

data class RecentIssueListItem(
    val id: Long,
    val title: String,
    val imgUrl: String
)
