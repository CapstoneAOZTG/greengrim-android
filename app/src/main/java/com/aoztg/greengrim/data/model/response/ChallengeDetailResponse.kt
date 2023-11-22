package com.aoztg.greengrim.data.model.response

data class ChallengeDetailResponse(
    val challengeInfo: ChallengeDetailInfo,
    val challengeTags: ChallengeDetailTags,
    val createdAt: String,
    val entered: Boolean
)

data class ChallengeDetailInfo(
    val description: String,
    val id: Int,
    val imgUrl: String,
    val title: String
)

data class ChallengeDetailTags(
    val category: String,
    val goalCount: String,
    val keyword: String,
    val participantCount: String,
    val ticketCount: String,
    val weekMinCount: String
)