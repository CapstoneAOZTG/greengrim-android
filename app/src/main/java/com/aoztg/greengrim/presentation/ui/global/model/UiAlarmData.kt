package com.aoztg.greengrim.presentation.ui.global.model

data class UiAlarmData(
    val type: String,
    val resourceId: Long,
    val imgUrl: String,
    val category: String,
    val createdAt: String,
    val boldDescription: String,
    val description: String,
    val isChecked: Boolean,
    val memberId: Long
)
