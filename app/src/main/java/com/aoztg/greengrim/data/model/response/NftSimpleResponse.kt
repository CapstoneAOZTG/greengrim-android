package com.aoztg.greengrim.data.model.response

data class NftSimpleResponse(
    val tokenId: Long,
    val imgUrl: String,
    val title: String,
    val nftId: Long,
    val traitsInfo: NftTraitsInfo
)

data class NftTraitsInfo(
    val background: String,
    val hair: String,
    val face: String,
    val gesture: String,
    val accessory: String,
    val shoes: String,
    val rarity: String
)
