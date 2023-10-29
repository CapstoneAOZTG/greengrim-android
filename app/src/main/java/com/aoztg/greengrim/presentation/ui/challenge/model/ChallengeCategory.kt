package com.aoztg.greengrim.presentation.ui.challenge.model

data class ChallengeCategory(
    val icon: Int,
    val title: String,
    val point: String,
    val onItemClicked: (String) -> Unit,
)
