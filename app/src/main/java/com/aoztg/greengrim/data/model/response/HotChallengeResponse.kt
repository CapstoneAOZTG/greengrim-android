package com.aoztg.greengrim.data.model.response

data class HotChallengeResponse(
    val hotChallengeInfos: List<HotChallengeInfo>
)

data class HotChallengeInfo(
    val challengeInfo: ChallengeInfo,
    val hotChallengeTags: HotChallengeTags
)

data class HotChallengeTags(
    val category: String,
    val keyword: String,
    val ticketCount: String
)

