package com.aoztg.greengrim.presentation.ui.nft.model

data class UiNftDetailInfo(
    val memberId : Long = -1,
    val nftId: Long = -1,
    val nftImage: String = "",
    val title: String = "",
    val description: String = "",
    val profileImage: String = "",
    val nickName: String = "",
    val date: String = "",
    val background: String = "",
    val hair: String = "",
    val face: String = "",
    val gesture: String = "",
    val accessory: String = "",
    val shoes: String = "",
    val rarity: String = "",
    val liked: Boolean = false,
    val mine: Boolean = false
)

