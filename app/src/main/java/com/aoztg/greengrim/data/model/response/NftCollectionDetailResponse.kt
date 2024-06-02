package com.aoztg.greengrim.data.model.response

data class NftCollectionDetailResponse(
    val nftId: Long,
    val imgUrl: String,
    val title: String,
    val tokenId: Long,
    val traitsInfo: NftTraitsInfo,
)
