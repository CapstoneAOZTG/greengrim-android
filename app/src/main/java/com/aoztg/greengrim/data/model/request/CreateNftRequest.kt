package com.aoztg.greengrim.data.model.request

data class CreateNftRequest(
    val password: String,
    val title: String,
    val description: String,
    val asset: String
)
