package com.aoztg.greengrim.data.model.response

data class GetProfileResponse(
    val id: Long,
    val nickName: String,
    val introduction: String,
    val profileImgUrl: String
)
