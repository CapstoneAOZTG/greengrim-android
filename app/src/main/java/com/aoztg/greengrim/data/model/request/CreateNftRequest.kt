package com.aoztg.greengrim.data.model.request

data class CreateNftRequest(
    val password: String,
    val grimId: Long,
    val title: String,
    val description: String,
)
