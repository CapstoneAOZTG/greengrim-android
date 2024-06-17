package com.aoztg.greengrim.presentation.ui.mypage.model

data class UiAnnounceData(
    val id: Long,
    val title: String,
    val createdAt: String,
    val onClickListener: (Long) -> Unit
)
