package com.aoztg.greengrim.data.model.response

data class HotNftResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<HotNftItem>
)

data class HotNftItem(
    val nftSimpleInfo: NftSimpleInfo,
    val memberSimpleInfo: MemberSimpleInfo,
    val likeCount: String
)

