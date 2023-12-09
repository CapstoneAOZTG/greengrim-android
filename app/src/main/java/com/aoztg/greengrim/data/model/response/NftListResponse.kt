package com.aoztg.greengrim.data.model.response

data class NftListResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<NftListItem>
)

data class NftListItem(
    val nftSimpleInfo: NftSimpleInfo,
    val memberSimpleInfo: MemberSimpleInfo,
    val price: String
)

data class NftSimpleInfo(
    val id: Int,
    val imgUrl: String,
    val title: String
)