package com.aoztg.greengrim.data.model.response

data class MyInfoResponse(
    val memberInfo: GetProfileResponse,
    val email: String,
    val point: Int,
    val wallet: Boolean
)
