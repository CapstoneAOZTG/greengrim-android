package com.aoztg.greengrim.data.model.response

data class HotChallengeResponse(
    val challengeInfos: List<HotChallengeInfo>
)

data class HotChallengeInfo(
    val id: Long,
    val title: String,
    val description: String,
    val imgUrl: String
)


