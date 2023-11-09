package com.aoztg.greengrim.presentation.ui.global.model

import com.aoztg.greengrim.data.model.ChallengeDetailTags

data class ChallengeDetail(
    val id: Int = -1,
    val imgUrl: String = "",
    val title: String = "",
    val description: String = "",
    val keywords: ChallengeDetailTags? = null,
    val date: String = "",
)
