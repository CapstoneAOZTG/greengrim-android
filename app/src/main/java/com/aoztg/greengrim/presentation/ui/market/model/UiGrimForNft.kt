package com.aoztg.greengrim.presentation.ui.market.model

data class UiGrimForNft(
    val id: Long = -1,
    val image: String = "",
    val title: String = "",
    val selectListener: (Long, String) -> Unit
)
