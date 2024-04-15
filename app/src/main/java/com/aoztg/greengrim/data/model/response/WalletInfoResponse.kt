package com.aoztg.greengrim.data.model.response

data class WalletInfoResponse(
    val existed : Boolean,
    val name: String?,
    val address: String?
)
