package com.aoztg.greengrim.presentation.ui.nft.model

data class UiNftDetailInfo(
    val nftId: Long = -1,
    val nftImage: String = "",
    val title: String = "",
    val description: String = "",
    val profileImage: String = "",
    val nickName: String = "",
    val date: String = "",
    val background : String = "",
    val hair : String = "",
    val face : String = "",
    val gesture : String = "",
    val accessory : String = "",
    val shoes : String = "",
    val liked : Boolean = false,
    val mine: Boolean = false
)

