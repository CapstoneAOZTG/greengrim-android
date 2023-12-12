package com.aoztg.greengrim.presentation.ui.info.model

data class UiMyCertificationList(
    val page: Int,
    val hasNext: Boolean,
    val result: List<UiMyCertification>
)

data class UiMyCertification (
    val id: Int,
    val categoryImg: String,
    val title: String,
    val category: String,
    val certificationImg: String,
    val certificationCount: String,
    val date: String,
    val description: String,
    val onItemClickListener: (Int) -> Unit
)
