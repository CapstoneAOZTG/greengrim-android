package com.aoztg.greengrim.presentation.ui.home.model

data class UiHotNftItem(
    val id: Long,
    val image: String,
    val title: String,
    val profileImage: String,
    val nickName: String,
    val likeCount: Int,
    val navigateToNftDetail: (Long) -> Unit,
)
