package com.aoztg.greengrim.data.model

data class GetProfileResponse(
    val id: Int,
    val nickName: String,
    val introduction: String,
    val profileImgUrl: String
)
