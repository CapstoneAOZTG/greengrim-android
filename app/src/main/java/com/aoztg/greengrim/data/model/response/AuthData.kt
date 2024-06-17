package com.aoztg.greengrim.data.model.response


data class AuthData(
    val tokenInfo: TokenData,
    val pushAlarmOn: Boolean,
    val chatAlarmOn: Boolean,
    val issueAlarmOn: Boolean,
    val noticeAlarmOn: Boolean
)

data class TokenData(
    val accessToken: String,
    val refreshToken: String,
    val memberId: Long
)
