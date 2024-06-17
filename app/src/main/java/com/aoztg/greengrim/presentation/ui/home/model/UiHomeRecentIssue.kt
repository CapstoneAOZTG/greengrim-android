package com.aoztg.greengrim.presentation.ui.home.model

data class UiHomeRecentIssue(
    val id: Long,
    val iconImg: String,
    val title: String,
    val itemClickListener: (Long) -> Unit
)
