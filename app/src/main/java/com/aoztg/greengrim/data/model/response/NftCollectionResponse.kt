package com.aoztg.greengrim.data.model.response

data class NftCollectionResponse(
    val page : Int,
    val hasNext : Boolean,
    val result : List<NftCollectionItem>
)

data class NftCollectionItem(
    val id: Long,
    val imgUrl: String,
    val title : String,
    val tokenId : String
)
