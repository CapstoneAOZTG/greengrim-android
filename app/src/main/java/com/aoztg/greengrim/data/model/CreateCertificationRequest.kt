package com.aoztg.greengrim.data.model

data class CreateCertificationRequest(
    val challengeId: Int,
    val imgUrl: String,
    val description: String,
    val round: Int
)
