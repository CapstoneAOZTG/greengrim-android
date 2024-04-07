package com.aoztg.greengrim.presentation.ui.home.model


data class UiHotChallenge(
    val id: Long,
    val imgUrl: String,
    val title: String,
    val description: String,
    val itemClickListener: (Long) -> Unit
)
