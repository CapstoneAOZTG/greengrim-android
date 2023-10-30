package com.aoztg.greengrim.presentation.ui.challenge.model

data class ChallengeCategory(
    val icon: Int,
    val category: String,
    val point: String,
    val onItemClicked: (String) -> Unit,
)
