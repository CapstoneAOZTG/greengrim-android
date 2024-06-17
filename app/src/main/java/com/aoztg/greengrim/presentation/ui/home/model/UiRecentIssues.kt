package com.aoztg.greengrim.presentation.ui.home.model

data class UiRecentIssues(
    val id: Long,
    val iconImg: String,
    val title: String,
    val itemClickListener: (Long) -> Unit
)
