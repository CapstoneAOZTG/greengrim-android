package com.aoztg.greengrim.data.model.response

data class NftDetailResponse(
    val memberSimpleInfo: MemberSimpleInfo,
    val nftInfo: NftDetailInfo,
    val contracts: String,
    val tokenId: String,
    val price: String,
    val marketed: Boolean,
    val mine: Boolean
)

data class NftDetailInfo(
    val id: Long,
    val imgUrl: String,
    val title: String,
    val description: String,
    val createdAt: String
)
