package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.UiCertificationDetail
import com.aoztg.greengrim.presentation.ui.toCategoryText


internal fun CertificationDetailResponse.toUiCertificationDetail(): UiCertificationDetail {
    return UiCertificationDetail(
        certificationId = certificationInfo.id,
        challengeTitle = challengeInfo.title,
        category = challengeInfo.category.toCategoryText(),
        ticketCount = challengeInfo.ticketCount,
        challengeDescription = challengeInfo.description,
        profileUrl = memberSimpleInfo.profileImgUrl,
        nickName = memberSimpleInfo.nickName,
        certificationImgUrl = certificationInfo.imgUrl,
        certificationTitle = certificationInfo.title,
        date = certificationInfo.createdAt,
        certificationDescription = certificationInfo.description,
        isVerified = isVerified
    )
}