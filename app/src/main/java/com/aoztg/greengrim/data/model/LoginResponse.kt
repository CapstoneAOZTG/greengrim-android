package com.aoztg.greengrim.data.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val memberId: Long
)
