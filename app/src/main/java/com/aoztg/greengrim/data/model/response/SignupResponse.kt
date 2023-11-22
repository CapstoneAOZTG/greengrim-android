package com.aoztg.greengrim.data.model.response

data class SignupResponse(
    val accessToken: String,
    val refreshToken: String,
    val memberId: Long
)
