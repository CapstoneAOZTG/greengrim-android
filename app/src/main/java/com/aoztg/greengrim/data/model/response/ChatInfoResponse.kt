package com.aoztg.greengrim.data.model.response

data class ChatInfoResponse(
    val category: String,
    val participantCount: String,
    val goalCount: String,
    val certificationCount: Int,
    val todayCertification: Boolean
)
