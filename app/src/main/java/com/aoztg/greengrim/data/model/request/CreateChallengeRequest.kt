package com.aoztg.greengrim.data.model.request

data class CreateChallengeRequest(
    val category: String,
    val title: String,
    val description: String,
    val imgUrl: String,
    val goalCount: Int,
)