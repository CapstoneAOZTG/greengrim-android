package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.presentation.ui.global.model.CertificationDetail


internal fun CertificationDetailResponse.toCertificationDetail(): CertificationDetail {
    return CertificationDetail(
        challengeTitle = this.challengeInfo.title,
        category = this.challengeInfo.category,
        ticketCount = this.challengeInfo.ticketCount,
        challengeDescription = this.challengeInfo.description,
        profileUrl = this.memberSimpleInfo.profileImgUrl,
        nickName = this.memberSimpleInfo.nickName,
        certificationImgUrl = this.certificationInfo.imgUrl,
        certificationTitle = this.certificationInfo.title,
        date = this.certificationInfo.createdAt,
        certificationDescription = this.certificationInfo.description,
        isVerified = this.isVerified
    )
}