package com.aoztg.greengrim.data.model.request

data class SignupRequest(
    val email: String,
    val nickName: String,
    val introduction: String,
    val profileImgUrl: String? = ""
)
