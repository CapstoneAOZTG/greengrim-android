package com.aoztg.greengrim.presentation.ui.global.model

import com.aoztg.greengrim.data.model.response.ChallengeDetailTags

data class UiChallengeDetail(
    val id: Long = -1,
    val imgUrl: String = "",
    val title: String = "",
    val description: String = "",
    val keywords: ChallengeDetailTags? = null,
    val date: String = "",
    val entered: Boolean = false,
)
