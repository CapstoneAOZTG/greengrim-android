package com.aoztg.greengrim.presentation.ui.home.model

import com.aoztg.greengrim.data.model.response.HotChallengeTags

data class UiHotChallenge(
    val id: Long,
    val imgUrl: String,
    val title: String,
    val keywords: HotChallengeTags,
    val itemClickListener: (Long) -> Unit
)
