package com.aoztg.greengrim.data.model.response

data class RecentIssueLIstResponse(
    val page: Int,
    val hasNext: Boolean,
    val result : List<IssueItem>
)
