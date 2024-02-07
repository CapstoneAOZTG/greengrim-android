package com.aoztg.greengrim.presentation.ui.chat.model

data class UiCertificationList(
    val page: Int,
    val hasNext: Boolean,
    val result: List<UiCertificationItem>
)

data class UiCertificationItem(
    val id: Long,
    val profileImg: String,
    val nick: String,
    val certificationImg: String,
    val certificationCount: String,
    val date: String,
    val description: String,
    val onItemClickListener: (Long) -> Unit
)
