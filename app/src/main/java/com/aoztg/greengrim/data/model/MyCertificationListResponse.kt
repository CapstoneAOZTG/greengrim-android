package com.aoztg.greengrim.data.model

data class MyCertificationListResponse(
    val hasNext: Boolean,
    val page: Int,
    val result: List<MyCertificationItem>
)

data class MyCertificationItem(
    val certificationInfo: CertificationInfo,
    val challengeTitleInfo: ChallengeTitleInfo
)

data class ChallengeTitleInfo(
    val category: String,
    val imgUrl: String,
    val title: String
)