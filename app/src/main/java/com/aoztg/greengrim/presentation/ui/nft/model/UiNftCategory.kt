package com.aoztg.greengrim.presentation.ui.nft.model

data class UiNftCategory(
    val img: Int,
    val categoryName : String,
    val count : Int,
    val navigateToCollectionList: (String) -> Unit
)
