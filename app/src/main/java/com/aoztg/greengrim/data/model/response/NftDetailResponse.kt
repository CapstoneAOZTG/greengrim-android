package com.aoztg.greengrim.data.model.response

data class NftDetailResponse(
    val memberSimpleInfo: MemberSimpleInfo,
    val nftInfo: NftDetailInfo,
    val tokenId: String,
    val traitsInfo: NftTraitsInfo,
    val mine: Boolean,
    val liked: Boolean
)

data class NftDetailInfo(
    val id: Long,
    val imgUrl: String,
    val title: String,
    val description: String,
    val createdAt: String
)
