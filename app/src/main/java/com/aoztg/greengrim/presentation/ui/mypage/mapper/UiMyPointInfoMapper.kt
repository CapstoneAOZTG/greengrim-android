package com.aoztg.greengrim.presentation.ui.mypage.mapper

import com.aoztg.greengrim.data.model.response.MyPointResponse
import com.aoztg.greengrim.data.model.response.PointInfo
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyPointInfo


fun PointInfo.toUiMyPointInfo() : UiMyPointInfo =
    UiMyPointInfo(
        imgUrl = imgUrl,
        title = title,
        date = date,
        point = "+ $point GP",
        wholePoint = "$totalPoint GP"
    )