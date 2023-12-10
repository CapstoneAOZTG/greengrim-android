package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.InfoBeforePurchaseNftResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiNftPurchaseData
import com.aoztg.greengrim.presentation.util.Constants.ONE_KLAY


internal fun InfoBeforePurchaseNftResponse.toUiNftPurchaseData() = UiNftPurchaseData(
    image = nftAndMemberInfo.nftSimpleInfo.imgUrl,
    title = nftAndMemberInfo.nftSimpleInfo.title,
    profileImage = nftAndMemberInfo.memberSimpleInfo.profileImgUrl,
    nickName = nftAndMemberInfo.memberSimpleInfo.nickName,
    price = price + " KLAY",
    fee = fee + " KLAY",
    total = total + " KLAY",
    balance = balance + " KLAY",
    balanceAfterPurchase = balanceAfterPurchase,
    totalWon = "≈ " + (total.toDouble() * ONE_KLAY).toString() + " KRW",
    marketId = marketId
)