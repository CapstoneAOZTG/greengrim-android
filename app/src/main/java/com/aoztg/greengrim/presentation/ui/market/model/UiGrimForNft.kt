package com.aoztg.greengrim.presentation.ui.market.model

data class UiGrimForNft(
    val id: Int = -1,
    val image: String = "",
    val title: String = "",
    val selectListener: (Int, String) -> Unit
)
