package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.CertificationDetail
import com.aoztg.greengrim.presentation.util.toCategoryText


internal fun CertificationDetailResponse.toCertificationDetail(): CertificationDetail {
    return CertificationDetail(
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