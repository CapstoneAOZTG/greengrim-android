package com.aoztg.greengrim.data.model.response

data class InfoBeforePurchaseNftResponse(
    val nftAndMemberInfo: InfoBeforePurchaseNftItem,
    val price: String,
    val fee: String,
    val total: String,
    val balance: String,
    val balanceAfterPurchase: String,
    val marketId: Long
)

data class InfoBeforePurchaseNftItem(
    val nftSimpleInfo: NftSimpleInfo,
    val memberSimpleInfo: MemberSimpleInfo
)
