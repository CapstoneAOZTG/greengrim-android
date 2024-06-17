package com.aoztg.greengrim.presentation.ui.home.model

data class UiRecentIssueDetail(
    val title: String = "",
    val description: String = "",
    val createdAt: String = "",
    val imgList: List<String> = emptyList()
)
