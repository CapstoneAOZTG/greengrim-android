package com.aoztg.greengrim.presentation.ui.nft.model

data class UiNftItem(
    val id: Long,
    val image: String,
    val title: String,
    val profileImage: String,
    val nickName: String,
    val isLiked: Boolean,
    val navigateToNftDetail: (Long) -> Unit,
    val clickLike : (Long) -> Unit
)
