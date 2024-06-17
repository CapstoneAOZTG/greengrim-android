package com.aoztg.greengrim.presentation.ui.home.model

data class UiRecentIssueItem(
    val id: Long,
    val img: String,
    val title: String,
    val onItemClickListener: (Long) -> Unit
)
