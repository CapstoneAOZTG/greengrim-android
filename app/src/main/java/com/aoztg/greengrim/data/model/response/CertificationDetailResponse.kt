package com.aoztg.greengrim.data.model.response

data class CertificationDetailResponse(
    val certificationInfo: CertificationInfo,
    val challengeInfo: ChallengeShortInfo,
    val memberSimpleInfo: MemberSimpleInfo,
    val isVerified: String
)

data class ChallengeShortInfo(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val ticketCount: String
)

data class CertificationInfo(
    val id: Int,
    val createdAt: String,
    val description: String,
    val imgUrl: String,
    val title: String
)

data class MemberSimpleInfo(
    val id: Int,
    val nickName: String,
    val profileImgUrl: String
)