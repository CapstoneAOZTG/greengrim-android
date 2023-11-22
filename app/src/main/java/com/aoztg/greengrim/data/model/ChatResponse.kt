package com.aoztg.greengrim.data.model

data class ChatResponse(
    val type: String = "",
    val senderId: Long = 0,
    val roomId: Int = 0,
    val message: String = "",
    val nickName: String = "",
    val profileImg: String = ""
)
