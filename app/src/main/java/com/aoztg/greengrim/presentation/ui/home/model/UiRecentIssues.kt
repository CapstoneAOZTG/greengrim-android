package com.aoztg.greengrim.presentation.ui.home.model

data class UiRecentIssues(
    val iconImg: String,
    val title : String,
    val link : String,
    val itemClickListener: (String) -> Unit
)
