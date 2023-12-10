package com.aoztg.greengrim.data.model.request

data class CreateNftRequest(
    val password: String,
    val grimId: Int,
    val title: String,
    val description: String,
)
