package com.aoztg.greengrim.presentation.ui.chat.model

data class CertificationListData(
    val page: Int,
    val hasNext: Boolean,
    val result: List<CertificationListItem>
)

data class CertificationListItem(
    val id: Int,
    val profileImg: String,
    val nick: String,
    val certificationImg: String,
    val certificationCount: String,
    val date: String,
    val description: String,
    val onItemClickListener: (Int) -> Unit
)
