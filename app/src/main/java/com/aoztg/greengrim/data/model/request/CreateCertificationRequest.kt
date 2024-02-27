package com.aoztg.greengrim.data.model.request

data class CreateCertificationRequest(
    val challengeId: Long,
    val imgUrl: String,
    val description: String,
    val round: Int
)
