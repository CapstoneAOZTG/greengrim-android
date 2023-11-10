package com.aoztg.greengrim.data.model

data class CertificationDetailResponse(
    val certificationInfo: CertificationInfo,
    val challengeInfo: ChallengeShortInfo,
    val memberSimpleInfo: MemberSimpleInfo,
    val verified: Boolean
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