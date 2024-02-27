package com.aoztg.greengrim.data.model.response

data class NftListResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<NftListItem>
)

data class NftListItem(
    val nftAndMemberInfo: NftAndMemberInfo,
    val price: String
)

data class NftAndMemberInfo(
    val nftSimpleInfo: NftSimpleInfo,
    val memberSimpleInfo: MemberSimpleInfo,
)

data class NftSimpleInfo(
    val id: Long,
    val imgUrl: String,
    val title: String
)