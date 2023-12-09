package com.aoztg.greengrim.presentation.ui.global.checkpassword

import com.aoztg.greengrim.data.model.request.CreateNftRequest

object FormBeforePasswordInput {

    var work: WorkType = WorkType.EMPTY
    var createNftRequest: CreateNftRequest?=null

    fun createNft(
        data : CreateNftRequest
    ){
        createNftRequest = data
    }

    fun purchaseNft(

    ){

    }

    fun sellNft(

    ){

    }
}

enum class WorkType{
    EMPTY,
    CREATE_NFT,
    PURCHASE_NFT,
    SELL_NFT
}