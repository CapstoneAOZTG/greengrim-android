package com.aoztg.greengrim.presentation.ui.mypage.model

data class UiMyInfo(
    val id: Long = -1,
    val nickName: String = "",
    val profileImgUrl: String = "",
    val introduction: String = "",
    val myPoint: String = "",
    val email: String = "",
    val hasWallet: Boolean = true,
    val walletName: String = "",
    val walletAddress : String = ""
){
    fun compareInfo(data: UiMyInfo): Boolean{
        return nickName == data.nickName &&
                profileImgUrl == data.profileImgUrl &&
                introduction == data.introduction &&
                myPoint == data.myPoint &&
                email == data.email
    }
}
