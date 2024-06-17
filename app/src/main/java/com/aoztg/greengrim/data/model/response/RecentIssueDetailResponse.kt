package com.aoztg.greengrim.data.model.response

data class RecentIssueDetailResponse(
    val title: String,
    val content: String,
    val createdAt: String,
    val imgUrls: List<String>
)
