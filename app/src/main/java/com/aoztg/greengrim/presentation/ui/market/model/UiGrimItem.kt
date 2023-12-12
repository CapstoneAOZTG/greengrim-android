package com.aoztg.greengrim.presentation.ui.market.model

data class UiGrimItem(
    val id: Int,
    val image: String,
    val title: String,
    val profileImage: String,
    val nickName: String,
    val navigateToGrimDetail: (Int) -> Unit
)
