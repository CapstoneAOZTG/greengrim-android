package com.aoztg.greengrim.presentation.ui.global.model

data class UiNftDetail(
    val nftId: Int = -1,
    val nftImage: String = "",
    val title: String = "",
    val description: String = "",
    val profileImage: String = "",
    val nickName: String = "",
    val date: String = "",
    val contractAddress: String = "",
    val tokenId: String = "",
    val price: String = "",
    val btnState: NftState = NftState.NONE,
)

enum class NftState{
    CAN_BUY,
    CAN_SELL,
    NONE
}
