package com.aoztg.greengrim.data.model.response

data class ChallengeDetailResponse(
    val challengeInfo: ChallengeDetailInfo,
    val challengeTags: ChallengeDetailTags,
    val createdAt: String,
    val entered: Boolean,
    val mine: Boolean
)

data class ChallengeDetailInfo(
    val description: String,
    val id: Long,
    val imgUrl: String,
    val title: String
)

data class ChallengeDetailTags(
    val category: String,
    val goalCount: String,
    val participantCount: String,
)