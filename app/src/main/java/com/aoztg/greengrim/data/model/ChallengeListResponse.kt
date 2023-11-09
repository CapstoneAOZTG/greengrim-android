package com.aoztg.greengrim.data.model

data class ChallengeListResponse(
    val hasNext: Boolean,
    val page: Int,
    val result: List<ChallengeListItem>
)

data class ChallengeListItem(
    val challengeInfo: ChallengeInfo,
    val challengeSimpleTags: ChallengeSimpleTags
)

data class ChallengeInfo(
    val description: String,
    val id: Int,
    val imgUrl: String,
    val title: String
)

data class ChallengeSimpleTags(
    val category: String,
    val goalCount: String,
    val keyword: String,
    val ticketCount: String
)