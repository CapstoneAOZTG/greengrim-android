package com.aoztg.greengrim.presentation.ui.home.model

data class UiMoreActivity(
    val imgResource: Int,
    val title: String,
    val description: String,
    val point: String,
    val itemClickListener: () -> Unit
)
