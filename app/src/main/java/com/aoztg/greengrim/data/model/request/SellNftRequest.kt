package com.aoztg.greengrim.data.model.request

data class SellNftRequest(
    val payPassword: String,
    val nftId: Int,
    val price: Double
)
