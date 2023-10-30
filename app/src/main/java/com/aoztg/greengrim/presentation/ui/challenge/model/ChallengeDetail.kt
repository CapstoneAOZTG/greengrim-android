package com.aoztg.greengrim.presentation.ui.challenge.model

data class ChallengeDetail(
    val imgUrl: String = "",
    val title: String = "",
    val description: String = "",
    val mainKeyword: List<String> = emptyList(),
    val subKeyword: List<String> = emptyList(),
    val date: String = "",
)
