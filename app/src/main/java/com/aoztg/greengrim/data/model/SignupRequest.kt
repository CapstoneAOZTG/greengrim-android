package com.aoztg.greengrim.data.model

data class SignupRequest(
    val email: String,
    val nickName: String,
    val introduction: String,
    val profileImgUrl: String? = ""
)
