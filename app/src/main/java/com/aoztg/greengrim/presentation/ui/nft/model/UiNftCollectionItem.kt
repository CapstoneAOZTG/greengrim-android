package com.aoztg.greengrim.presentation.ui.nft.model

data class UiNftCollectionItem(
    val id: Long,
    val image: String,
    val title: String,
    val number: String,
    val navigateToDetail: (Long) -> Unit
)
