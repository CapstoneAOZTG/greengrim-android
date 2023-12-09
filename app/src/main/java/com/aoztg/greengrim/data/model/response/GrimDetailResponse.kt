package com.aoztg.greengrim.data.model.response

data class GrimDetailResponse(
    val grimInfo: GrimDetailItem,
    val createdAt: String,
    val mine: Boolean
)

data class GrimDetailItem(
    val id: Int,
    val imgUrl: String,
    val title: String,
    val memberSimpleInfo: MemberSimpleInfo
)
