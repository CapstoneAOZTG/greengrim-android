package com.aoztg.greengrim.data.model.response

data class GrimListResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<GrimListItem>
)

data class GrimListItem(
    val id: Int,
    val imgUrl: String,
    val title: String,
    val memberSimpleInfo: MemberSimpleInfo
)
