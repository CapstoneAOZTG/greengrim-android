package com.aoztg.greengrim.presentation.ui.global.checkpassword

import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.request.PurchaseNftRequest
import com.aoztg.greengrim.data.model.request.SellNftRequest

object FormBeforePasswordInput {

    var work: WorkType = WorkType.EMPTY
    var createNftRequest: CreateNftRequest? = null
    var purchaseNftRequest: PurchaseNftRequest? = null
    var sellNftRequest: SellNftRequest? = null

    fun createNft(
        data: CreateNftRequest
    ) {
        work = WorkType.CREATE_NFT
        createNftRequest = data
    }

    fun purchaseNft(
        data: PurchaseNftRequest
    ) {
        work = WorkType.PURCHASE_NFT
        purchaseNftRequest = data
    }

    fun sellNft(
        data: SellNftRequest
    ) {
        work = WorkType.SELL_NFT
        sellNftRequest = data
    }
}

enum class WorkType {
    EMPTY,
    CREATE_NFT,
    PURCHASE_NFT,
    SELL_NFT
}