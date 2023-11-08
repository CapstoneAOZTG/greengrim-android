package com.aoztg.greengrim.presentation.ui.challenge.model

import com.aoztg.greengrim.data.model.ChallengeSimpleTags


data class ChallengeListData(
    val hasNext: Boolean,
    val page: Int,
    val result: List<ChallengeRoom>
)

data class ChallengeRoom(
    val id: Int,
    val imgUrl: String,
    val title: String,
    val keywords: ChallengeSimpleTags,
    val onItemClicked: (String) -> Unit,
)

