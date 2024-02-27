package com.aoztg.greengrim.presentation.ui.challenge.model

import com.aoztg.greengrim.data.model.response.ChallengeSimpleTags


data class UiChallengeList(
    val hasNext: Boolean,
    val page: Int,
    val result: List<UiChallengeRoom>
)

data class UiChallengeRoom(
    val id: Long,
    val imgUrl: String,
    val title: String,
    val keywords: ChallengeSimpleTags,
    val onItemClicked: (Long) -> Unit,
)

