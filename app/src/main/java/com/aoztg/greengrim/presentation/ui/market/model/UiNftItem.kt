package com.aoztg.greengrim.presentation.ui.market.model

data class UiNftItem(
    val id: Long,
    val image: String,
    val price: String,
    val title: String,
    val profileImage: String,
    val nickName: String,
    val navigateToNftDetail: (Long) -> Unit
)
