package com.aoztg.greengrim.presentation.ui.home.mapper

import com.aoztg.greengrim.data.model.response.HomeInfoResponse
import com.aoztg.greengrim.presentation.ui.home.model.HomeInfo


fun HomeInfoResponse.toHomeInfo() = HomeInfo(
    id = id,
    nickName = "$nickName 님의 탄소 저감량",
    carbonReduction = "$carbonReduction g"
)