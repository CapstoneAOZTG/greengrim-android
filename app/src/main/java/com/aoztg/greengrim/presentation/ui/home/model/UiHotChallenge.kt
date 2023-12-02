package com.aoztg.greengrim.presentation.ui.home.model

import com.aoztg.greengrim.data.model.response.HotChallengeTags

data class UiHotChallenge(
    val id: Int,
    val imgUrl: String,
    val title: String,
    val keywords: HotChallengeTags,
    val itemClickListener: (Int) -> Unit
)
