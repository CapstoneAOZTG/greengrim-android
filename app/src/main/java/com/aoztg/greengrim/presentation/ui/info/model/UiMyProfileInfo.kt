package com.aoztg.greengrim.presentation.ui.info.model

data class UiMyProfileInfo(
    val id: Int = -1,
    val nickName: String = "",
    val profileImgUrl: String = "",
    val introduction: String = "",
    val greenPoint: String = "",
    val greenCoin: String = "",
    val hasWallet: Boolean = false
)
