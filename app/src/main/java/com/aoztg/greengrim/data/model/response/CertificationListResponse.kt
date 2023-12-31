package com.aoztg.greengrim.data.model.response

data class CertificationListResponse(
    val hasNext: Boolean,
    val page: Int,
    val result: List<CertificationItem>
)

data class CertificationItem(
    val certificationInfo: CertificationInfo,
    val memberSimpleInfo: MemberSimpleInfo
)